<!--Balance.vue-->
<!--SER 421 | Lab 2-->
<!--Jacob Smith (jwsmit24)-->

<template>
  <div class="balanceContainer">
    <h2>Account Balance: {{ balance }} {{currency}}</h2>
    <p> Amount: {{ localAmount }} {{ currency }}</p>

<!--    slider-->
    <input type="range" min="5" max="100" step="5" v-model.number="localAmount">
    <span>{{ localAmount }}</span>

    <div class="buttonRow">
      <button @click="add">Add</button>
<!--      disable subtract button if the amount is greater than balance-->
      <button @click="subtract" :disabled="localAmount > balance">Subtract</button>
    </div>
  </div>

</template>

<style scoped>

.buttonRow {
  display: flex;
  gap: 1rem;
}

.balanceContainer {
  padding: 1rem;
  background-color: lightgray;
  border: 1px solid blue;
  border-radius: 5px;;
  width: 50%;
  display: grid;
  gap: 1rem;

}
</style>

<script>
import {defineComponent} from 'vue'

export default defineComponent({
  name: "Balance",
  props: {
    balance: Number,
    amount: Number,
    currency: String
  },
  data() {
    return {
      localAmount: this.amount
    }
  },
  emits: ["update:balance", "update:amount"],
  watch: {
    localAmount(newVal) {
      this.$emit("update:amount", newVal)
    },
    amount(newVal) {
      this.localAmount = newVal;
    },
  },
  methods: {
    add() {
      this.$emit("update:balance", this.balance + this.localAmount);
    },
    subtract() {
      if (this.localAmount <= this.balance) {
        this.$emit("update:balance", this.balance - this.localAmount)
      }
    }
  }
})
</script>