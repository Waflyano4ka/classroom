<script setup lang="ts">
import { useRoute } from 'vue-router'
import AppLogo from '@/components/app-bar/AppLogo.vue'

const route = useRoute()
</script>

<template>
  <v-app-bar
    color="primary"
    class="px-2"
    elevation="0"
  >
    <app-logo class="text-none"/>

    <v-spacer />

    <router-link
      v-if="route.name !== 'login'"
      :to="{ name: 'login' }"
      custom
      v-slot="{ navigate }"
    >
      <v-btn
        class="text-none login-button"
        color="background"
        size="large"
        append-icon="mdi-login"
        rounded="lg"
        variant="tonal"
        @click="navigate"
      >
        Войти
      </v-btn>
    </router-link>
  </v-app-bar>
</template>

<style scoped>
.login-button {
  font-size: 20px;
}
</style>
