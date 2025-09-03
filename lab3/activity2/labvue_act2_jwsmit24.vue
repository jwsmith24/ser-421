<!--SER 421 Lab 2 Activity 2-->
<!--Jacob Smith (jwsmit24)-->
<!--Vue SFC Playground link: https://play.vuejs.org/#eNqNVu9v2zYQ/VcuGoZ2S/wjSbcBrhOs6/qhG9BuXfZhiPJBls42G4rURMqOa/h/7yMpUYpbBA0CmOTdPb53dzx7n7yqqvGm4WSWzC2XlcwsX6cqVUTzQmywJPzNT0YjWgolzJoLKoSB345GVLMquCaxpP+VpnVmiB9y5gJOqikXMGmYGjZWaGVoNOrwAE2bkVhepYkwv2vFaUK5zIzBQeG3rSfRf7oB8oYp16DHFth2zUAVn05ozuU1HGoyua6ZtmCw3/v14UC6se7+/T4yGEtWK7s+HOYTBLZcJkHnQKlQVNV6VbMxvdgj7iwNUyQ5ryL9NWfICQR4XnlTI0k28Jt9K7nqSeC/2wj6DtHI+ymdHw4OO0Ld4vhuCDSoZauxLZ1LZabM1pWqOqoSPGW2YAm1S12DQb7WImeXnrAyqNrsnnfRNKhi9IhggBOqgmy7qxgedVYIjQiVlW4bWDjETSYbdxIxN6NSFyxx1BiuX3WOkwH0ft9yosMhsp94+kd19utFYy0S+GsuRX4PXNMsSmEj8gxVzxaSC5hOhndev/9zPgnBX4H1rybuUzWfPHpSODB5LSqLHT9UurZU8DJrpKW9Cygymz3/IawJBbJNrbod0WQSXxIZC8zOgFrPaHoWt10TzOg2TX6mH+kXgowzSpOLS9cr025709feauIN1zu7FmpFAmW76wGDT8B7cRFiLy/Dp9v3rm3Rvec0OJy3H63/cdxA3b9vH+vq8z5DAMI6Q/uWpmHvy33wRjchGgwIvIVgC7Olz2nMKnSasXs711ft+ugVvvwCvGS71gW0tVjDlhneACWYh21ehQEpTIDcnuHZ5DVnmBqef+cO3+eeQS+Xrq46Vm3qbzu+d4OLKPh4tNPTl777IueWCUYYW9zsnl2mCio1BimKjf6zsVM6/+4SgD06GjLzlXiUnFThfz6Jne263O6kl1lxgZNxmFyB+VIrO9qyWK3tjBZaFh7MnxrxCWU9v5h+jzOAIrJtqEHoMiuF3M3o2WuMVwHUd7x9dgZhSpsqyzmGuq+REJdrqdFDmOes+tsi0I0ocQNg6IMuMwUw6BXLL3j91POCXCcR2pKzxEKoWorV+KPRCt+j/k5MLzSjkFy/D2M1TWLnYNZJqbd/+DNbN9x2tpt4nN9/5fyjeXBnafKXK2i9wVCMNpvVK7bB/OafdyjswIi52Uh4P2H8wEbLxveBd/utUQVoD/w827elG1iYDjfmzYNlZTpRjmj/StIEPyZePyG9p3s5ftE1UHL4DPenvc4=-->

<template>

  <div>
    <!-- finished display - render if qno has exceeded number of questions -->
    <div v-if="isDone" class="done">
      You have completed the quiz! <em>Your score was {{score}} out of {{questions.length}}</em>
    </div>

    <!-- in progress display -->
    <div v-else >
      <p class="header">Your current score: {{score}} out of {{questions.length}}</p>
      <p class="header">Question #{{qno + 1}}: {{questions[qno]}}</p>
      <div>
        <!-- render the answer options -->
        <label v-for="choice in choices" :key="choice" class="choices">
          <input type="radio" name="answer" :value="choice" v-model="userAnswer" />
          {{ choice }}
        </label>
      </div>
      <button @click="submitAnswer" :disabled="!userAnswer">OK</button>
    </div>

  </div>

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
  },
  methods: {
    submitAnswer() {
      // if answer is correct, increase score
      if (this.userAnswer === this.qanswers[this.qno]) {
        this.score++;
      }
      // reset input and move to next question
      this.qno++;
      this.userAnswer = "";
    }
  }
}
</script>

<style scoped>
.header {
  font-weight: bold;
  font-size: 120%;
}

.choices {
  font-family: 'Courier New', monospace;
}

.done {
  color: green;
  font-family: 'Times New Roman', serif;
  font-size: 150%;
}

</style>
