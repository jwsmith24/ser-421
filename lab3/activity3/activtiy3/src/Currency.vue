<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "Currency",
  props: {
    currencyFrom: String,
    amount: Number
  },
  emits: ['update:currencyFrom', 'update:amount'],
  data() {
    return {
      localAmount: this.amount,
      localCurrencyFrom: this.currencyFrom,
      currencyTo: "USD"
      //todo: pull in currency file from class repo to make sure logic meshes
    }
  },
  watch: {
    localAmount(newVal) {
      this.$emit("update:amount", newVal);
    },
    amount(newVal) {
      this.localAmount = newVal;
    },
    currencyFrom(newVal) {
      this.localCurrencyFrom = newVal;
    },
    localCurrencyFrom(newVal) {
      this.$emit("update:currencyFrom", newVal);
    }
  }
})
</script>

<template>

  <div>
    <h2>Currency Converter</h2>

    <label><input type="number" v-model.number="localAmount"></label>

    <label>Convert From:
      <select v-model="localCurrencyFrom">
        <option>USD</option>
        <option>EUR</option>
        <option>JPY</option>
      </select>
    </label>

    <label>Convert To: <select v-model="currencyTo">
      <option >USD</option>
      <option >EUR</option>
      <option >JPY</option>
    </select></label>

    <p>{{localAmount}} {{ localCurrencyFrom}} exchanges to </p>
<!--    tbd-->
  </div>
</template>

<style scoped>

</style>