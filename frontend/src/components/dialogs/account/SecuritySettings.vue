<script setup lang="ts">
import { inject, ref } from 'vue'
import { passwordConfirmationRule, passwordRule, requiredRule } from "@/utils/validation.ts";
import { MESSAGES } from "@/constants/messages.ts";
import { API_PATHS } from "@/constants/apiPaths.ts";
import axios from "axios";
import api from "@/services/api.ts";

import type { SnackbarType } from "@/types/Snackbar.ts";

const formRef = ref()
const visibleCurrentPassword = ref(false)
const visiblePassword = ref(false)
const visibleRePassword = ref(false)

const currentPassword = ref('')
const password = ref('')
const passwordConfirmation = ref('')

const isLoading = ref(false)
const showSnackbar = inject<
  (message: string, type?: SnackbarType) => void
>('showSnackbar')

const changePassword = async () => {
  const { valid } = await formRef.value.validate()
  if (!valid) return

  isLoading.value = true
  try {
    await api.put(API_PATHS.AUTH.CHANGE_PASSWORD, {
      currentPassword: currentPassword.value,
      newPassword: password.value,
    })

    currentPassword.value = ''
    password.value = ''
    passwordConfirmation.value = ''
    formRef.value.resetValidation()

    showSnackbar?.(MESSAGES.AUTH.CHANGE_PASSWORD_SUCCESS, 'success')
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.AUTH.CHANGE_PASSWORD_ERROR, 'error'
      )
    } else {
      showSnackbar?.(MESSAGES.AUTH.CHANGE_PASSWORD_ERROR, 'error')
    }
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="account-settings d-flex flex-column h-100">
    <div class="flex-grow-1 overflow-y-auto px-2 pt-2">
      <v-form ref="formRef" @submit.prevent="changePassword">
        <v-text-field
          v-model="currentPassword"
          label="Текущий пароль"
          :type="visibleCurrentPassword ? 'text' : 'password'"
          variant="outlined"
          color="primary"
          base-color="darkprimary"
          rounded="lg"
          class="mb-1"
          :rules="[requiredRule, passwordRule]"
          :append-inner-icon="visibleCurrentPassword ? 'mdi-eye-off' : 'mdi-eye'"
          @click:append-inner="visibleCurrentPassword = !visibleCurrentPassword"
        />

        <v-text-field
          v-model="password"
          label="Пароль"
          :type="visiblePassword ? 'text' : 'password'"
          variant="outlined"
          color="primary"
          base-color="darkprimary"
          rounded="lg"
          class="mb-1"
          :rules="[requiredRule, passwordRule]"
          :append-inner-icon="visiblePassword ? 'mdi-eye-off' : 'mdi-eye'"
          @click:append-inner="visiblePassword = !visiblePassword"
        />

        <v-text-field
          v-model="passwordConfirmation"
          label="Подтверждение пароля"
          :type="visibleRePassword ? 'text' : 'password'"
          variant="outlined"
          color="primary"
          base-color="darkprimary"
          rounded="lg"
          class="mb-1"
          :rules="[
          requiredRule,
          (value) => passwordConfirmationRule(value, password),
        ]"
          :append-inner-icon="visibleRePassword ? 'mdi-eye-off' : 'mdi-eye'"
          @click:append-inner="visibleRePassword = !visibleRePassword"
        />
      </v-form>
    </div>

    <v-toolbar density="compact" color="transparent" class="flex-shrink-0 pa-0 me-0 mt-2">
      <v-spacer />
      <v-btn
        color="primary"
        variant="flat"
        size="large"
        rounded="lg"
        class="text-background font-weight-bold text-none me-0"
        :loading="isLoading"
        @click="changePassword"
      >
        Сменить пароль
      </v-btn>
    </v-toolbar>
  </div>
</template>

<style scoped>

</style>
