<script lang="js">
import {defineComponent} from 'vue'

export default defineComponent({
  props: {
    name: { type: String, required: true}
  }
})
</script>

<template>
<h1>Hello {{ name }}</h1>
</template>

<style scoped>

</style>