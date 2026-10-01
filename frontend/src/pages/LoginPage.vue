<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const visiblePassword = ref(false)
const router = useRouter()
</script>

<template>
  <div class="login-form pa-2 text-background">
    <h1 class="text-h4 text-center mb-8">
      Авторизация
    </h1>

    <v-form>
      <v-text-field
        label="Логин/Почта"
        type="email"
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

      <div class="d-flex flex-column flex-sm-row ga-3 mt-2">
        <v-btn
          color="background"
          variant="flat"
          size="large"
          rounded="lg"
          class="text-darkprimary font-weight-bold text-none flex-grow-1 order-1 order-sm-2"
        >
          Войти
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
.login-form {
  width: 100%;
  max-width: 500px;
}
</style>
