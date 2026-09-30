<template>
  <home-app-bar />

  <v-main class="bg-background">
    <router-view />
  </v-main>
</template>

<script setup lang="ts">
import HomeAppBar from '@/components/app-bar/HomeAppBar.vue'
</script>
