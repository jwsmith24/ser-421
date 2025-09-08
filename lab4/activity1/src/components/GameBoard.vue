<script>

export default {
  name: "GameBoard",
  data() {
    return {
      categories: [], // store the possible categories
      questions: [], // stores trivia questions from the API
      loading: false, // loading status
      error: null // error message if one exists
    }
  },
  methods: {
    async fetchCategories() {
      this.loading = true;
      this.error = null;

      try {
        const response = await fetch("https://opentdb.com/api_category.php");
        const data = await response.json();

        console.log("got categories", data);

      } catch (error) {
        this.error = "Failed to retrieve categories";
        console.error(error);
      } finally {
        this.loading = false; // reset loading state whether it failed or succeeded
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
      <article class="categories">CAT 1</article>
      <article class="categories">CAT 2</article>
      <article class="categories">CAT 3</article>
      <article class="categories">CAT 4</article>


  </div>

</template>

<style scoped>

.gameBoardContainer {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(5, 1fr);
  gap: 1rem;
  text-align: center;
}

.categories {
  color: white;
  font-weight: bold;
  font-size: x-large;
}

</style>