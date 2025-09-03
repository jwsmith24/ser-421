<!--HelloWorld.vue-->
<!--SER 421 | Lab 2-->
<!--Jacob Smith (jwsmit24)-->

<script>
import {defineComponent} from 'vue'

export default defineComponent({
  props: {
    name: { type: String, required: true}
  }
})
</script>

<template>
<h1 class="greeting">Hello, {{ name }}</h1>
</template>

<style scoped>
.greeting {
  color: cadetblue;
}
</style>