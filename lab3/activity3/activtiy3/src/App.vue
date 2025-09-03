<!--App.vue-->
<!--SER 421 | Lab 2-->
<!--Jacob Smith (jwsmit24)-->

<!-- app.vue is the single source of truth for all components-->
<template>
  <div class="container">
    <HelloWorld :name="username"/>
    <Balance
             v-model:balance="balance"
             v-model:amount="amount"
             :currency="currencyFrom"
    />
    <Currency
      v-model:currencyFrom="currencyFrom"
      v-model:amount="amount"
      v-model:balance="balance"
    />
  </div>
</template>

<style scoped>
.container {
  padding: 0;
  margin: 0;
}
</style>

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
      amount: 200,
      currencyFrom: "USD"
    }
  }
}

</script>
