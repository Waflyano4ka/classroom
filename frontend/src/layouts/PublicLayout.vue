<template>
  <welcome-app-bar />

  <v-main class="d-flex flex-column bg-background" style="min-height: 100vh">
    <div class="bg-primary text-white px-6 pt-4 pb-12 flex-shrink-0"/>

    <v-container fluid class="mt-n16 px-6 pb-6 flex-grow-1 d-flex">
      <v-card
        rounded="lg"
        elevation="3"
        class="pa-6 flex-grow-1 d-flex desc-border"
      >
        <div class="bg-darkprimary placeholder-area flex-grow-1 d-flex align-center justify-center">
          <router-view />
        </div>
      </v-card>
    </v-container>
  </v-main>
</template>

<script setup lang="ts">
import WelcomeAppBar from '@/components/app-bar/WelcomeAppBar.vue'
</script>

<style scoped>
.desc-border {
  background-color: #664a40;
}

.placeholder-area {
  border: 2px solid #49322e;
  border-radius: 8px;
  min-height: 0;
}
</style>
