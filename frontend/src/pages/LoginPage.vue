<script setup lang="ts">
import { inject, ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { API_PATHS } from '@/constants/apiPaths'
import { MESSAGES } from '@/constants/messages'
import axios from "axios"
import api from '@/services/api'

import type {VForm} from "vuetify/components"
import type { SnackbarType } from '@/types/snackbar.ts'
import type { LoginResponse } from '@/types/auth'

import { requiredRule } from "@/utils/validation.ts"

const visiblePassword = ref(false)
const password = ref('')
const login = ref('')

const isLoading = ref(false)

const router = useRouter()
const authStore = useAuthStore()
const form = ref<VForm | null>(null)
const showSnackbar = inject<
  (message: string, type?: SnackbarType) => void
>('showSnackbar')

const authorization = async () => {
  const { valid } = await form.value!.validate()
  if (!valid) return

  isLoading.value = true

  try {
    const response = await api.post<LoginResponse>(
      API_PATHS.AUTH.LOGIN,
      {
        login: login.value,
        password: password.value,
      },
    )

    authStore.setToken(response.data.token)
    showSnackbar?.(MESSAGES.AUTH.LOGIN_SUCCESS, 'success')
    await router.push({ name: 'home' })
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.AUTH.LOGIN_ERROR,
        'error'
      )
    } else {
      showSnackbar?.(MESSAGES.AUTH.LOGIN_ERROR, 'error')
    }
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="login-form pa-2 text-background">
    <h1 class="text-h4 text-center mb-8">
      Авторизация
    </h1>

    <v-form ref="form" @submit.prevent="authorization">
      <v-text-field
        v-model="login"
        label="Логин/Почта"
        type="email"
        variant="outlined"
        rounded="lg"
        class="mb-2"
        :rules="[requiredRule]"
      />

      <v-text-field
        v-model="password"
        label="Пароль"
        :type="visiblePassword ? 'text' : 'password'"
        variant="outlined"
        rounded="lg"
        class="mb-8"
        :rules="[requiredRule]"
        :append-inner-icon="visiblePassword ? 'mdi-eye-off' : 'mdi-eye'"
        @click:append-inner="visiblePassword = !visiblePassword"
      />

      <div class="d-flex flex-column flex-sm-row ga-3 mt-2">
        <v-btn
          color="background"
          variant="flat"
          size="large"
          rounded="lg"
          class="text-darkprimary font-weight-bold text-none flex-grow-1 order-1 order-sm-2"
          type="submit"
          :loading="isLoading"
        >
          Войти
        </v-btn>

        <router-link
          :to="{ name: 'welcome' }"
          custom
          v-slot="{ navigate }"
        >
          <v-btn
            variant="outlined"
            size="large"
            rounded="lg"
            class="text-none font-weight-bold order-2 order-sm-1"
            @click="navigate"
          >
            Назад
          </v-btn>
        </router-link>
      </div>
    </v-form>
  </div>
</template>

<style scoped>
.login-form {
  width: 100%;
  max-width: 500px;
}
</style>
