<script setup lang="ts">
import { provide, ref } from 'vue'
import type { SnackbarType } from '@/types/snackbar.ts'

interface SnackbarMessage {
  text: string
  color: SnackbarType
}

const snackbarQueue = ref<SnackbarMessage[]>([])

const showSnackbar = (
  text: string,
  type: SnackbarType = 'info',
) => {
  snackbarQueue.value.push({
    text,
    color: type,
  })
}

provide('showSnackbar', showSnackbar)
</script>

<template>
  <v-app>
    <router-view />

    <v-snackbar-queue
      v-model="snackbarQueue"
      location="top end"
      :timeout="3000"
      total-visible="4"
      transition="bouncy-slide-auto"
      closable
      variant="tonal"
    >
      <template #actions="{ props }">
        <v-icon-btn
          aria-label="Закрыть"
          icon="$close"
          size="small"
          variant="text"
          v-bind="props"
        />
      </template>
    </v-snackbar-queue>
  </v-app>
</template>
