import { ref } from 'vue'
import { defineStore } from "pinia";
import { useCurrentUserStore } from "@/stores/currentUser.ts";
import type { CurrentUserUpdate } from "@/types/CurrentUserUpdate.ts";

export const useCurrentUserUpdateStore = defineStore('accountEdit', () => {
  const draft = ref<CurrentUserUpdate | null>(null)

  function initDraft(): void {
    if (draft.value) return

    const { user } = useCurrentUserStore()
    if (!user) return

    draft.value = {
      username: user.username ?? '',
      email: user.email ?? '',
      lastName: user.lastName ?? null,
      firstName: user.firstName ?? null,
      middleName: user.middleName ?? null,
    }
  }

  function clearDraft(): void {
    draft.value = null
  }

  return {
    draft,
    initDraft,
    clearDraft,
  }
})
