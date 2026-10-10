<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

import AppLogo from '@/components/app-bar/AppLogo.vue'
import AccountDialog from "@/components/dialogs/AccountDialog.vue";

const router = useRouter()
const authStore = useAuthStore()

//todo подсказка при наведении

const logout = async () => {
  authStore.clearToken()
  await router.push({ name: 'welcome' })
}
</script>

<template>
  <v-app-bar
    color="primary"
    class="px-2"
    elevation="0"
  >
    <app-logo class="text-none"/>

    <v-spacer />

    <template v-slot:append>
      <account-dialog/>

      <v-btn
        class="text-none logout-button"
        color="logout"
        icon="mdi-logout"
        rounded="lg"
        variant="tonal"
        @click="logout"
      />
    </template>
  </v-app-bar>
</template>

<style scoped>
.logout-button {
  font-size: 20px;
}
</style>

