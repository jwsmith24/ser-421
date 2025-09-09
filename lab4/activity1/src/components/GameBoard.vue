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
      loading: false, // loading status
      error: null, // error message if one exists
      QUESTIONS_PER_CATEGORY,
      // modal state
      showModal: false,
      modalCategory: null,
      modalQuestion: null,
      modalKey: null,
      modalValue: null
    }
  },
  methods: {
    async fetchCategories() {
      this.loading = true;
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
        this.loading = false; // reset loading state whether it failed or succeeded
      }

    },
    async handleQuestionClick(category, rowIndex) {
      const key = `${category.id}-${rowIndex}`; // used for updating after answer

      // determine difficulty
      let difficulty = "medium";
      if (rowIndex === 0 || rowIndex === 1) difficulty = "easy";
      else if (rowIndex === 4) difficulty = "hard";

      const questionData = await this.fetchQuestion(category.id, difficulty);
      const question = questionData?.results[0] // grab the question data
      // do nothing if question doesn't exist
      if (!question) return;

      console.log("got question: ", question);

      // setup modal
      this.modalCategory =  category;
      this.modalQuestion = question;
      this.modalValue = (rowIndex + 1) * 100;
      this.modalKey = key;
      this.showModal = true;
    },
    async fetchQuestion(categoryId, difficulty) {
      try {
        console.log(`fetching questions with cat: ${categoryId} and diff: ${difficulty}`);
        const response = await fetch(`https://opentdb.com/api.php?amount=1&category=${categoryId}&difficulty=${difficulty}&type=boolean`)
        return await response.json();
      } catch(error) {
        this.error = "Failed to fetch question";
        console.error(error);
      }
    },
    selectNextPlayer() {

      if (this.currentPlayerIndex === this.players.length - 1) {
        this.currentPlayerIndex = 0;
      } else {
        this.currentPlayerIndex++;
      }
    },



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

    <p v-if="loading">Loading categories..</p>
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
            @click="handleQuestionClick(category, i - 1)"
        >
          ${{ i  * 100 }}
        </article>
      </div>
    </template>
  </div>

<!--  modal for questions -->
  <div v-if="showModal" class="modalOverlay" @click.self="closeModal">
    <div>
      <h3>{{ modalCategory.name }} - ${{ modalValue }}</h3>
      <p v-html="modalQuestion.question"></p>
      <div class="modalButtons">
        <button @click="answerQuestion('True')">True</button>
        <button @click="answerQuestion('False')">False</button>
      </div>
    </div>
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

</style>