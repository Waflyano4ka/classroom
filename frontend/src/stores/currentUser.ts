import { ref } from 'vue'
import { defineStore } from 'pinia'
import api from '@/services/api'
import { API_PATHS } from '@/constants/apiPaths'
import type { CurrentUser } from '@/types/CurrentUser'

export const useCurrentUserStore = defineStore('currentUser', () => {
  const user = ref<CurrentUser | null>(null)
  const isLoaded = ref(false)

  async function fetchCurrentUser(): Promise<void> {
    const response = await api.get<CurrentUser>(API_PATHS.USERS.ME)

    user.value = response.data
    isLoaded.value = true
  }

  function clearCurrentUser(): void {
    user.value = null
    isLoaded.value = false
  }

  return {
    user,
    isLoaded,
    fetchCurrentUser,
    clearCurrentUser,
  }
})
