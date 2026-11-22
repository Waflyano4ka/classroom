<script setup lang="ts">
import { inject, ref } from "vue";
import { leaveClassroom } from "@/services/classroom.ts";
import { MESSAGES } from "@/constants/messages.ts";
import axios from "axios";

import type { SnackbarType } from "@/types/Snackbar.ts";

const props = defineProps<{
  memberId: string
}>()

const emit = defineEmits<{
  onLeave: []
}>()

const dialog = ref(false);
const loading = ref(false);

const showSnackbar = inject<
  (message: string, type?: SnackbarType) => void
>('showSnackbar')

async function onLeave() {
  loading.value = true

  try {
    const response = await leaveClassroom(props.memberId)

    showSnackbar?.(
      `${MESSAGES.CLASSROOM_MEMBERSHIP.LEAVE_SUCCESS} «${response.data.name}»`,
      'success'
    )

    emit('onLeave')
    dialog.value = false
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.CLASSROOM_MEMBERSHIP.LEAVE_ERROR, 'error'
      )
    } else {
      showSnackbar?.(MESSAGES.CLASSROOM_MEMBERSHIP.LEAVE_ERROR, 'error')
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
          title="Покинуть"
          prepend-icon="mdi-exit-to-app"
          class="text-error"
          v-bind="activatorProps"
        />
      </template>

      <template #default="{ isActive }">
        <v-card class="pa-4 text-darkprimary" color="background">
          <div class="d-flex align-center justify-space-between mb-2">
            <h1 class="text-h5 font-weight-bold mt-0 mb-0">
              Покинуть класс?
            </h1>
          </div>

          <p class="text-body-2 text-medium-emphasis mb-4 mt-1 px-2 text-darkprimary">
            Вы действительно хотите покинуть этот класс?<br/>
            Чтобы снова стать его участником, потребуется вступить в него повторно.
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
              @click="onLeave"
            >
              Покинуть
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
