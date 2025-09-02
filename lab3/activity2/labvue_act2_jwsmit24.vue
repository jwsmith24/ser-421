<script setup>
import { ref } from 'vue'

const msg = ref('Hello World!')
</script>

<template>
  <h1>{{ msg }}</h1>
  <input v-model="msg" />
</template>


<script>
export default {
  data() {
    return {
      // question state
      qno: 0,
      questions: ["6 * 7 =", "23 + 10 =", "The answer to everything is"],
      qanswers: ["42", "33", "42"],
      choices: ["0", "1", "13", "33", "42"],

      // UI state
      userAnswer: "",
      score: 0
    }
  },
  computed: {
    isDone() {
      return this.qno >= this.questions.length;
    }
  }
}
</script>