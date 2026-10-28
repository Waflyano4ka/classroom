<script setup lang="ts">
import { ref } from 'vue'

const searchQuery = ref('')
</script>

<template>
  <v-container class="page-wrapper py-6" fluid>
    <v-sheet class="sheet bg-accentbackground">
      <v-text-field
        :loading="loading"
        append-inner-icon="mdi-magnify"
        density="compact"
        label="Поиск..."
        variant="solo"
        class="bg-accentbackground mt-3 mx-3"
        hide-details
        single-line
        @click:append-inner="onClick"
      ></v-text-field>
    </v-sheet>
    <v-sheet class="sheet bg-accentbackground">
    </v-sheet>
  </v-container>
</template>

<style scoped>
.page-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
  overflow-y: auto;
  min-height: 0;

  scrollbar-width: thin;
  scrollbar-color: #356859 transparent;
}

.sheet {
  width: 90%;
  max-width: 1800px;
  flex-shrink: 0;
  border-radius: 8px;
  /* временно */
  min-height: 200px;
}
</style>
