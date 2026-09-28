<script setup lang="ts">
import { onMounted, ref } from 'vue'
import api from './services/api'

const message = ref('Загрузка...')

onMounted(async () => {
  try {
    const response = await api.get('/test')
    message.value = response.data
  } catch (error) {
    message.value = 'Ошибка при обращении к backend'
    console.error(error)
  }
})
</script>

<template>
  <main>
    <h1>{{ message }}</h1>
  </main>
</template>
