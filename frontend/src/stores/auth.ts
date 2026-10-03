import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(
    localStorage.getItem('accessToken'),
  )

  const setToken = (value: string) => {
    token.value = value
    localStorage.setItem('accessToken', value)
  }

  const clearToken = () => {
    token.value = null
    localStorage.removeItem('accessToken')
  }

  return {
    token,
    setToken,
    clearToken,
  }
})
