<script setup lang="ts">
import HomeAppBar from "@/components/app-bar/HomeAppBar.vue";
</script>

<template>
  <home-app-bar />
  <v-main class="main-layout">
    <router-view />
  </v-main>
</template>

<style scoped>
.main-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;           /* или 100dvh */
  overflow: hidden;        /* запрещаем общий скролл */
}
</style>
