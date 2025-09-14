<script>
// set for constant lookup
const BAD_IDS = new Set([10, 13, 21, 26, 27, 29, 30, 32]);
const CATEGORY_COUNT = 4;
const QUESTIONS_PER_CATEGORY = 5;

// helper to remove bad categories that don't support the game constraints
function filterCategories(data) {
  return data.filter((category) =>
      !BAD_IDS.has(Number(category.id)));
}

function getRandomIndex(size) {
  return Math.floor(Math.random() * size);
}
// helper to select 4 random, unique categories from the filtered list
function selectCategories(data) {
  const selected = [];
  let randomIndex = getRandomIndex(data.length);

  while (selected.length < CATEGORY_COUNT) {
    const choice = data[randomIndex];
    // if category isn't already selected, add it otherwise skip it
    if (!selected.some((selected) => choice.id === selected.id)) {
      selected.push(choice);
    }

    randomIndex = getRandomIndex(data.length) // get new random index
  }

  console.log("selected: ", selected)
  return selected;
}


export default {
  name: "GameBoard",
  data() {
    return {
      categories: [], // store the possible categories
      board: {}, // stores trivia questions from the API
      players: [
        {id: 1, balance: 0},
        {id: 2, balance: 0},
        {id: 3, balance: 0}
      ],
      currentPlayerIndex: 0,
      categoryLoading: false,
      questionLoading: false,
      error: null, // error message if one exists
      QUESTIONS_PER_CATEGORY,
      gameOver: false,
      winner: null,
      usedQuestions: {}, // key: "categoryId-rowIndex" , value: {playerId, correct}

      // modal state
      showModal: false,
      modalCategory: null,
      modalQuestion: null,
      modalKey: null,
      modalValue: null,
      feedback: null, // correct or incorrect

    }
  },
  methods: {
    async fetchCategories() {
      this.categoryLoading = true;
      this.error = null;
      try {
        const response = await fetch("https://opentdb.com/api_category.php");
        const data = await response.json();
        // filter out bad categories (use set for constant lookup)
        const filtered = filterCategories(data.trivia_categories);
        this.categories = selectCategories(filtered);

      } catch (error) {
        this.error = "Failed to retrieve categories";
        console.error(error);
      } finally {
        this.categoryLoading = false; // reset loading state whether it failed or succeeded
      }

    },
    async handleQuestionClick(category, rowIndex) {

      if (this.questionLoading) return; // prevent spamming question

      this.questionLoading = true;

      try {
        const key = `${category.id}-${rowIndex}`; // used for updating after answer

        // determine difficulty
        let difficulty = "medium";
        if (rowIndex === 0 || rowIndex === 1) difficulty = "easy";
        else if (rowIndex === 4) difficulty = "hard";

        const questionData = await this.fetchQuestion(category.id, difficulty);

        const question = questionData?.results[0] // grab the question data
        // do nothing if question doesn't exist
        if (!question) {
          console.log("No question received from API -- may have exceeded rate limit/retries. Please try again later.");
          return;
        }

        // setup modal
        this.modalCategory =  category;
        this.modalQuestion = question;
        this.modalValue = (rowIndex + 1) * 100;
        this.modalKey = key;
        this.showModal = true;

        console.log("got question: ", question);
        console.log("Modal opened with question: ", this.modalQuestion);

      } finally {
        this.questionLoading = false;
      }


    },
    async fetchQuestion(categoryId, difficulty, retries = 3, backoff = 2000) {
      try {
        console.log(`fetching questions with cat: ${categoryId} and diff: ${difficulty}`);
        const response = await fetch(`https://opentdb.com/api.php?amount=1&category=${categoryId}&difficulty=${difficulty}&type=boolean`)

        // trivia api limits calls to once per 5 seconds
        if (response.status === 429) {
          if (retries > 0) { // limit to 3 retries so we don't wait forever
            console.warn(`Rate limit exceeded, retrying in ${backoff / 1000} seconds..`);
            await new Promise((r) => setTimeout(r, backoff));
            return this.fetchQuestion(categoryId, difficulty, retries - 1, backoff * 2); // try again with longer delay
          } else {
            throw new Error("Rate limit exceeded and no retries left..");
          }
        }

        if (!response.ok) {
          throw new Error(`Http error: ${response.status}`);
        }

        return await response.json();
      } catch(error) {
        this.error = "Failed to fetch question";
        console.error(error);
        return null;
      }
    },
    selectNextPlayer() {

      if (this.currentPlayerIndex === this.players.length - 1) {
        this.currentPlayerIndex = 0;
      } else {
        this.currentPlayerIndex++;
      }
    },
    closeModal() {
      this.showModal = false;
      this.modalCategory = null;
      this.modalQuestion = null;
      this.modalKey = null;
      this.modalValue = null;
      this.feedback = null;
    },
    answerQuestion(answer) {
      if (!this.modalQuestion) return;

      const correct = this.modalQuestion.correct_answer === answer;

      // show feedback message in modal
      this.feedback = correct ? "Correct!" : "Incorrect!";

      // update player balance
      const player = this.players[this.currentPlayerIndex];

      if (correct) {
        player.balance += this.modalValue; // gain money on correct answer
      } else {
        player.balance -= this.modalValue; // lose money on wrong answer
      }

      // update game board cell
      if (this.modalKey) {
        this.usedQuestions[this.modalKey] = {
          playerId: player.id,
          correct
        }
      }

      // cycle to next player if they got the question wrong
      if (!correct) {
        this.selectNextPlayer();
      }

      // short delay so the player can see feedback then close modal and check game status
      setTimeout(() => {
        this.closeModal();
        this.checkGameOver();
      }, 1500);
    },
    // helper for applying correct and incorrect styles to question cells
    determineCellClass(key) {
      const result = this.usedQuestions[key];
      if (!result) return {};
      return result.correct ? 'correctCell' : 'incorrectCell';
    },
    checkGameOver() {
      const totalCells = this.categories.length * this.QUESTIONS_PER_CATEGORY;
      const usedCells = Object.keys(this.usedQuestions).length;

      if (usedCells === totalCells) {
        this.gameOver = true;
        this.calculateWinner();
      }

    },

    calculateWinner() {
      let maxBalance = Math.max(...this.players.map(player => player.balance));
      let winners = this.players.filter(player => player.balance === maxBalance); // could be multiple if there's a tie

      if (winners.length === 1) {
        this.winner = `Player ${winners[0].id} wins with $${winners[0].balance}!`;
      } else {
        this.winner = `It's a tie! ${winners.map(player => player.id).join(" & ")} win with $${maxBalance}!`;
      }
    },

    restartGame() {
      this.players.forEach(p => p.balance = 0);
      this.currentPlayerIndex = 0;
      this.usedQuestions = {};
      this.showModal = false;
      this.modalCategory = null;
      this.modalQuestion = null;
      this.modalKey = null;
      this.modalValue = null;
      this.feedback = null;
      this.gameOver = false;
      this.winner = null;

      // get new categories
      this.fetchCategories();
    }



  },
  mounted() {
    this.fetchCategories(); // fetch categories on mount
  }
}

</script>

<template>
  <div  class="playerDisplay">
    <div v-for="(p, index) in players"
         class="playerInfo"
         :key="p.id"
         :class="{ activePlayer: index === currentPlayerIndex }"
    >
      <p>Player {{ p.id}}</p>
      <p>Balance: {{p.balance}}</p>
    </div>

  </div>
  <div class="gameBoardContainer">

    <p v-if="categoryLoading">Loading categories..</p>
    <p v-else-if="error">{{ error }}</p>

    <template v-else>
      <div
          v-for="category in categories"
          :key="category.id"
          class="questionColumn"
      >
        <article class="cell categories">{{ category.name }}</article>

        <article
            v-for="i in QUESTIONS_PER_CATEGORY"
            :key="`${category.id}-${i}`"
            class="cell questionCell"
            :class="determineCellClass(`${category.id}-${i-1}`)"
            @click="!usedQuestions[`${category.id}-${i-1}`] && handleQuestionClick(category, i - 1)"
        >
          <template v-if="!usedQuestions[`${category.id}-${i-1}`]">
            ${{ i * 100 }}
          </template>
          <template v-else>
            P{{ usedQuestions[`${category.id}-${i-1}`].playerId }}
          </template>
        </article>
      </div>
    </template>
    <div v-if="questionLoading">Avoiding rate limit...</div>
  </div>

<!--  modal for questions -->
  <div v-if="showModal" class="modalOverlay" @click.self="closeModal">
    <div v-if="modalQuestion" class="modalContent">
      <h3>{{ modalCategory.name }} - ${{ modalValue }}</h3>
      <p v-html="modalQuestion.question"></p>
      <p v-if="feedback" :style="{ color: feedback === 'Correct!' ? 'green' : 'red' }">
        {{ feedback }}
      </p>
      <div class="modalButtons">
        <button @click="answerQuestion('True')" id="trueButton">True</button>
        <button @click="answerQuestion('False')" id="falseButton">False</button>
      </div>
    </div>
  </div>

<!--  game over modal-->
  <div v-if="gameOver" class="gameOverModal">
    <h2>Game Over!</h2>
    <p>{{ winner }}</p>
    <button @click="restartGame">Play Again</button>
  </div>

</template>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.playerDisplay {
  display: flex;
  gap: 1rem;
}
.playerInfo:hover {
  cursor: pointer;
  opacity: 90%;
}

.activePlayer {
  color: goldenrod;
  font-weight: bold;
  border: 3px solid goldenrod;
}

.playerInfo {
  border: 1px solid white;
  padding: 0.5rem;
  border-radius: 5px;
}

.gameBoardContainer {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: .8rem;
  text-align: center;
  height: 100vh;
}

.questionColumn {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.questionCell {
  background: #444;
  color: goldenrod;
  font-weight: bold;
  font-size: 1rem;
  padding: 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: transform 0.2s ease;
  display: grid;
  align-items: center;
}


.correctCell {
  background: green;
  color: white;
  transform: none;
  pointer-events: none;
  cursor: default;
}

.incorrectCell {
  background: red;
  color: white;
  transform: none;
  pointer-events: none;
  cursor: default;
}


.questionCell:hover {
  transform: scale(1.05);
  background: #555;
}

.categories {
  color: white;
  font-weight: bold;
  font-size: 1.1rem;
  padding: 0.5rem;
  border-radius: 4px;
  min-height: 8rem;
  display: grid;
  align-items: center;

}

.modalOverlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
}

.modalButtons {
  display: flex;
  justify-content: center;
  gap: 1rem;

}

.gameOverModal button {
  margin-top: 1.5rem;
  padding: 0.75rem 1.5rem;
  background: goldenrod;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s ease;
}

.gameOverModal button:hover {
  background: #daa520;
}

.modalButtons > * {
  padding: .5rem;
}

.modalButtons > *:hover {
  border: 2px solid white;
}

#trueButton {
  background-color: green;
  color: white;
}

#falseButton {
  background-color: red;
  color: white
}



.modalContent {
  display: grid;
  gap: 1rem;
  border: 2px solid white;
  padding: 1rem;
  border-radius: 5px;
}

.gameOverModal {
  position: fixed;
  top: 0; left: 0;
  width: 100vw; height: 100vh;
  background: rgba(0,0,0,0.9);
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 3000;
}

.gameOverModal h2 {
  font-size: 2rem;
  margin-bottom: 1rem;
}

</style>