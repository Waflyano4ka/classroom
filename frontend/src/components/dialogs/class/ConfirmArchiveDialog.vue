<script setup lang="ts">
import { inject, ref } from "vue";
import {archiveClassroom, leaveClassroom} from "@/services/classroom.ts";
import { MESSAGES } from "@/constants/messages.ts";
import axios from "axios";

import type { SnackbarType } from "@/types/Snackbar.ts";

const props = defineProps<{
  memberId: string
}>()

const emit = defineEmits<{
  onArchive: []
}>()

const dialog = ref(false);
const loading = ref(false);

const showSnackbar = inject<
  (message: string, type?: SnackbarType) => void
>('showSnackbar')

async function onArchive() {
  loading.value = true

  try {
    const response = await archiveClassroom(props.memberId)

    showSnackbar?.(
      `${MESSAGES.CLASSROOM.ARCHIVE_SUCCESS} «${response.data.name}»`,
      'success'
    )

    emit('onArchive')
    dialog.value = false
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.CLASSROOM.ARCHIVE_ERROR, 'error'
      )
    } else {
      showSnackbar?.(MESSAGES.CLASSROOM.ARCHIVE_ERROR, 'error')
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="text-center confirm-class-wrapper">
    <v-dialog
      v-model="dialog"
      max-width="600"
      width="90%"
      content-class="settings-dialog"
    >
      <template #activator="{ props: activatorProps }">
        <v-list-item
          title="Архивировать"
          prepend-icon="mdi-archive-outline"
          class="text-error"
          v-bind="activatorProps"
        />
      </template>

      <template #default="{ isActive }">
        <v-card class="pa-4 text-darkprimary" color="background">
          <div class="d-flex align-center justify-space-between mb-2">
            <h1 class="text-h5 font-weight-bold mt-0 mb-0">
              Архивировать класс?
            </h1>
          </div>

          <p class="text-body-2 text-medium-emphasis mb-4 mt-1 px-2 text-darkprimary">
            Вы действительно хотите архивировать этот класс?
          </p>

          <v-toolbar density="compact" color="transparent" class="flex-shrink-0 pa-0 mt-2">
            <v-spacer />
            <v-btn
              color="primary"
              variant="flat"
              size="large"
              rounded="lg"
              class="text-background font-weight-bold text-none me-2"
              @click="isActive.value = false"
            >
              Отмена
            </v-btn>
            <v-btn
              color="logout"
              variant="tonal"
              size="large"
              rounded="lg"
              class="text-none me-0"
              :loading="loading"
              @click="onArchive"
            >
              Архивировать
            </v-btn>
          </v-toolbar>
        </v-card>
      </template>
    </v-dialog>
  </div>
</template>

<style scoped>
.confirm-class-wrapper {
  width: 100%;
}

@media (max-width: 800px) {
  .confirm-class-wrapper {
    width: 100%;
  }
}
</style>
