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
      loading: false, // loading status
      error: null, // error message if one exists
      QUESTIONS_PER_CATEGORY
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
    handleQuestionClick(category, rowIndex) {
      console.log(`Clicked ${category.name}, row ${rowIndex + 1}`)
      let difficulty = "";
      switch (rowIndex) {
        case 0:
        case 1 :
          difficulty = "easy";
          break;
        case 4:
          difficulty = "hard";
          break;
        default:
          difficulty = "medium";
      }
      const question = this.fetchQuestion(category.id, difficulty);
      console.log("got question: ", question);
    },
    async fetchQuestion(categoryId, difficulty) {
      try {
        console.log(`fetching questions with cat: ${categoryId} and diff: ${difficulty}`);
        const response = await fetch(`https://opentdb.com/api.php?amount=1&category=${categoryId}&difficulty=${difficulty}`)
        return await response.json();
      } catch(error) {
        this.error = "Failed to fetch question";
        console.error(error);
      }
    }

  },
  mounted() {
    this.fetchCategories(); // fetch categories on mount

  }
}



</script>

<template>
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
          Q {{ i }}
        </article>
      </div>
    </template>
  </div>
</template>

<style scoped>

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