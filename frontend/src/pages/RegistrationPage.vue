<script setup lang="ts">
import { inject, ref } from 'vue'
import { useRouter } from 'vue-router'
import { API_PATHS } from '@/constants/apiPaths'
import { MESSAGES } from '@/constants/messages'
import axios from 'axios'
import api from '@/services/api'

import type { VForm } from 'vuetify/components'
import type { SnackbarType } from '@/types/snackbar.ts'

import UserAgreementDialog from '@/components/dialogs/UserAgreementDialog.vue'

import {
  passwordRule,
  requiredRule,
  passwordConfirmationRule,
  usernameRule,
  emailRule,
  agreementRule
} from '@/utils/validation'

const visiblePassword = ref(false)
const visibleRePassword = ref(false)
const agreementDialog = ref(false)

const password = ref('')
const passwordConfirmation = ref('')
const username = ref('')
const email = ref('')
const agreed = ref(false)

const isLoading = ref(false)

const router = useRouter()
const form = ref<VForm | null>(null)
const showSnackbar = inject<
  (message: string, type?: SnackbarType) => void
>('showSnackbar')

const register = async () => {
  const { valid } = await form.value!.validate()
  if (!valid) return

  isLoading.value = true

  try {
    await api.post(API_PATHS.AUTH.REGISTER, {
      email: email.value,
      username: username.value,
      password: password.value,
    })

    showSnackbar?.(MESSAGES.AUTH.REGISTER_SUCCESS, 'success')
    await router.push({ name: 'login' })
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.AUTH.REGISTER_ERROR,
        'error'
      )
    } else {
      showSnackbar?.(MESSAGES.AUTH.REGISTER_ERROR, 'error')
    }
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="registration-form pa-2 text-background">
    <h1 class="text-h4 text-center mb-8">
      Регистрация
    </h1>

    <v-form ref="form" @submit.prevent="register">
      <v-text-field
        v-model="email"
        label="Почта"
        type="email"
        variant="outlined"
        rounded="lg"
        class="mb-2"
        :rules="[requiredRule, emailRule]"
      />

      <v-text-field
        v-model="username"
        label="Логин"
        variant="outlined"
        rounded="lg"
        class="mb-2"
        :rules="[requiredRule, usernameRule]"
      />

      <v-text-field
        v-model="password"
        label="Пароль"
        :type="visiblePassword ? 'text' : 'password'"
        variant="outlined"
        rounded="lg"
        class="mb-2"
        :rules="[requiredRule, passwordRule]"
        :append-inner-icon="visiblePassword ? 'mdi-eye-off' : 'mdi-eye'"
        @click:append-inner="visiblePassword = !visiblePassword"
      />

      <v-text-field
        v-model="passwordConfirmation"
        label="Подтверждение пароля"
        :type="visibleRePassword ? 'text' : 'password'"
        variant="outlined"
        rounded="lg"
        class="mb-2"
        :rules="[
          requiredRule,
          (value) => passwordConfirmationRule(value, password),
        ]"
        :append-inner-icon="visibleRePassword ? 'mdi-eye-off' : 'mdi-eye'"
        @click:append-inner="visibleRePassword = !visibleRePassword"
      />

      <v-checkbox
        v-model="agreed"
        class="mb-8"
        :rules="[agreementRule]"
      >
        <template #label>
          <span class="ml-2">
            Я согласен на обработку персональных данных и с условиями
            <a
              href="#"
              class="agreement"
              @click.stop.prevent="agreementDialog = true"
            >
              пользовательского соглашения
            </a>
          </span>
        </template>
      </v-checkbox>

      <user-agreement-dialog
        v-model="agreementDialog"
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
          :disabled="!agreed"
        >
          Зарегистрироваться
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
  .registration-form {
    width: 100%;
    max-width: 500px;
  }

  .agreement {
    color: #d6ba88;
    font-weight: bold;
  }
</style>
