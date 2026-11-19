<script setup lang="ts">
import {inject, ref} from "vue";
import { inviteCodeRule } from "@/utils/validation.ts";
import type {ClassroomColor} from "@/types/ClassroomColor.ts";
import axios from "axios";
import {MESSAGES} from "@/constants/messages.ts";
import type {SnackbarType} from "@/types/Snackbar.ts";
import api from "@/services/api.ts";
import {API_PATHS} from "@/constants/apiPaths.ts";

const dialog = ref(false);
const formRef = ref()
const loading = ref(false);

const form = ref({
  inviteCode: ''
})

const emit = defineEmits<{
  joined: []
}>()

const showSnackbar = inject<
  (message: string, type?: SnackbarType) => void
>('showSnackbar')

async function submit() {
  const { valid } = await formRef.value.validate()
  if (!valid) return

  loading.value = true
  try {
    const response = await  api.post(API_PATHS.CLASSROOM_MEMBERSHIPS.JOIN, {
      inviteCode: form.value.inviteCode
    })

    showSnackbar?.(
      `${MESSAGES.CLASSROOM_MEMBERSHIP.JOIN_SUCCESS} «${response.data.name}»`,
      'success'
    )

    emit('joined')
    dialog.value = false
    form.value.inviteCode = ''
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.CLASSROOM_MEMBERSHIP.JOIN_ERROR, 'error'
      )
    } else {
      showSnackbar?.(MESSAGES.CLASSROOM_MEMBERSHIP.JOIN_ERROR, 'error')
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="text-center join-class-wrapper">
    <v-dialog
      v-model="dialog"
      max-width="600"
      width="90%"
      content-class="settings-dialog"
    >
      <template #activator="{ props: activatorProps }">
        <v-btn
          variant="flat"
          rounded="pill"
          color="accentbackground"
          size="large"
          class="filter-btn"
          v-bind="activatorProps"
        >
          Подключиться
        </v-btn>
      </template>

      <template #default="{ isActive }">
        <v-card class="pa-4 text-darkprimary" color="background">
          <div class="d-flex align-center justify-space-between mb-2">
            <h1 class="text-h5 font-weight-bold mt-0 mb-0">
              Подключиться к классу
            </h1>

            <v-btn
              icon
              variant="text"
              @click="isActive.value = false"
            >
              <v-icon size="32">mdi-close-circle</v-icon>
            </v-btn>
          </div>

          <p class="text-body-2 text-medium-emphasis mb-4 mt-1 px-2 text-darkprimary">
            Для подключения к классу потребуется код приглашения.<br>
            Получить его можно у преподавателя или создателя класса.<br>
            Введите шестизначный код, чтобы присоединиться.
          </p>

          <v-form ref="formRef" class="flex-grow-1 overflow-y-auto px-2 pt-2 thin-scroll" @submit.prevent="submit">
            <v-text-field
              v-model="form.inviteCode"
              label="Код подключения"
              variant="outlined"
              color="primary"
              base-color="darkprimary"
              rounded="lg"
              maxlength="6"
              counter="6"
              @update:model-value="form.inviteCode = $event.toUpperCase()"
              class="mb-1"
              :rules="[inviteCodeRule]"
            />
          </v-form>

          <v-toolbar density="compact" color="transparent" class="flex-shrink-0 pa-0 mt-2">
            <v-spacer />
            <v-btn
              color="primary"
              variant="flat"
              size="large"
              rounded="lg"
              class="text-background font-weight-bold text-none me-0"
              :loading="loading"
              @click="submit"
            >
              Подключиться
            </v-btn>
          </v-toolbar>
        </v-card>
      </template>
    </v-dialog>
  </div>
</template>

<style scoped>
.join-class-wrapper {
  width: 100%;
}

@media (max-width: 800px) {
  .join-class-wrapper {
    width: 100%;
  }

  .filter-btn {
    width: 100% !important;
  }
}
</style>
