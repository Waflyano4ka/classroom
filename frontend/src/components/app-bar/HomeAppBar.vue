<template>
  <v-app-bar
    color="primary"
    class="px-2"
  >
    <app-logo class="text-none"/>

    <v-spacer />

    <v-btn
      class="text-none login-button"
      color="background"
      size="large"
      append-icon="mdi-login"
      rounded="lg"
      variant="tonal"
    >
      Войти
    </v-btn>
  </v-app-bar>
</template>

<script setup lang="ts">
import AppLogo from '@/components/app-bar/AppLogo.vue'
</script>

<style scoped>
.login-button {
  font-size: 20px;
}
</style>
