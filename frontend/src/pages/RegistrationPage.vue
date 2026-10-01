<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

import UserAgreementDialog from '@/components/dialogs/UserAgreementDialog.vue'

const visiblePassword = ref(false)
const visibleRePassword = ref(false)
const router = useRouter()
const agreed = ref(false)
const agreementDialog = ref(false)
</script>

<template>
  <div class="registration-form pa-2 text-background">
    <h1 class="text-h4 text-center mb-8">
      Регистрация
    </h1>

    <v-form>
      <v-text-field
        label="Почта"
        type="email"
        variant="outlined"
        rounded="lg"
        class="mb-2"
      />

      <v-text-field
        label="Логин"
        variant="outlined"
        rounded="lg"
        class="mb-2"
      />

      <v-text-field
        label="Пароль"
        :type="visiblePassword ? 'text' : 'password'"
        variant="outlined"
        rounded="lg"
        class="mb-2"
        :append-inner-icon="visiblePassword ? 'mdi-eye-off' : 'mdi-eye'"
        @click:append-inner="visiblePassword = !visiblePassword"
      />

      <v-text-field
        label="Подтверждение пароля"
        :type="visibleRePassword ? 'text' : 'password'"
        variant="outlined"
        rounded="lg"
        class="mb-2"
        :append-inner-icon="visibleRePassword ? 'mdi-eye-off' : 'mdi-eye'"
        @click:append-inner="visibleRePassword = !visibleRePassword"
      />

      <v-checkbox
        v-model="agreed"
        class="mb-8"
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
        >
          Зарегистрироваться
        </v-btn>

        <v-btn
          variant="outlined"
          size="large"
          rounded="lg"
          class="text-none font-weight-bold order-2 order-sm-1"
          @click="router.back()"
        >
          Назад
        </v-btn>
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
    color: #7d645c;
    font-weight: bold;
  }
</style>
