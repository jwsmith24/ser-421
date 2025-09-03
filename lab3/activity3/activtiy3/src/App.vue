

<!-- app.vue is the single source of truth for all components-->
<template>
  <div>
    <HelloWorld :name="username"/>
    <Balance
             v-model:balance="balance"
             v-model:amount="amount"
             :currency="currencyFrom"
    />
    <Currency
      v-model:currencyFrom="currencyFrom"
      v-model:amount="amount"
    />
  </div>
</template>

<style scoped></style>

<script>
import HelloWorld from "@/HelloWorld.vue";
import Currency from "@/Currency.vue";
import Balance from "@/Balance.vue";

export default {
  components: {HelloWorld, Balance, Currency},
  data() {
    return {
      username: "Jake",
      balance: 200,
      amount: 30,
      currencyFrom: "USD"
    }
  }
}

</script>
