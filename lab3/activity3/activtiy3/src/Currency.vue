<!--Currency.vue-->
<!--SER 421 | Lab 2-->
<!--Jacob Smith (jwsmit24)-->

<script>
import {defineComponent} from 'vue'

export default defineComponent({
  name: "Currency",
  props: {
    currencyFrom: String,
    amount: Number,
  },
  emits: ['update:currencyFrom', 'update:amount'],
  data() {
    return {
      currencyOptions: [
        {name: "USD", desc: "US Dollar"},
        {name: "EUR", desc: "Euro"},
        {name: "INR", desc: "Indian Rupee"},
        {name: "BHD", desc: "Bahraini Dinar"}
      ],
      localAmount: this.amount,
      localCurrencyFrom: this.currencyFrom,
      currencyTo: "USD"}
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
  },
  computed: {
    finalAmount: function() {
      let to = this.currencyTo;
      let from = this.currencyFrom;
      let finalAmount;

      switch(from) {
      case "INR":
        if (to === "USD") {
          finalAmount = this.amount * 0.016;
        }
        if (to === "EUR") {
          finalAmount = this.amount * 0.013;
        }
        if (to === "INR") {
          finalAmount = this.amount;
        }
        if (to === "BHD") {
          finalAmount = this.amount * 0.0059;
        }
        break;
      case "USD":
        if (to === "INR") {
          finalAmount = this.amount * 63.88;
        }
        if (to === "EUR") {
          finalAmount = this.amount * 0.84;
        }
        if (to === "USD") {
          finalAmount = this.amount;
        }
        if (to === "BHD") {
          finalAmount = this.amount * 0.38;
        }
        break;
      case "EUR":
        if (to === "INR") {
          finalAmount = this.amount * 76.22;
        }
        if (to === "USD") {
          finalAmount = this.amount * 1.19;
        }
        if (to === "EUR") {
          finalAmount = this.amount;
        }
        if (to === "BHD") {
          finalAmount = this.amount * 0.45;
        }
        break;
      case "BHD":
        if (to === "INR") {
          finalAmount = this.amount *169.44;
        }
        if (to === "USD") {
          finalAmount = this.amount * 2.65;
        }
        if (to === "EUR") {
          finalAmount = this.amount * 2.22;
        }
        if (to === "BHD") {
          finalAmount = this.amount;
        }
        break;
      }
      return finalAmount;

    }
  },
  methods: {
    swap() {
      let temp = this.localCurrencyFrom;
      this.localCurrencyFrom = this.currencyTo;
      this.currencyTo = temp;
    }
  }
})
</script>

<template>

  <div class="currencyContainer">
    <h2>Currency Converter</h2>

    <label><input type="number" v-model.number="localAmount"></label>

    <label>Convert From:
      <select v-model="localCurrencyFrom">
        <option v-for="option in currencyOptions" :key="option.name" :value="option.name">
          {{ option.name }} ({{ option.desc}})
        </option>
      </select>
    </label>

    <label>Convert To: <select v-model="currencyTo">
      <option v-for="option in currencyOptions" :key="option.name" :value="option.name">
        {{ option.name }} ({{ option.desc}})
      </option>
    </select></label>
    <button @click="swap">Swap</button>

    <p>{{localAmount}} {{ localCurrencyFrom}} exchanges to {{ finalAmount}} {{currencyTo}}</p>
<!--    tbd-->
  </div>
</template>

<style scoped>

.currencyContainer {
  margin-top: 1rem;
  background-color: floralwhite;
  padding: 1rem;
  border: 1px solid blue;
  border-radius: 5px;
  display: grid;
  gap: 1rem;
  width: 50%;
}

</style>