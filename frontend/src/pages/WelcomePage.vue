<script setup lang="ts">

</script>

<template>
  <div class="text-center text-background">
    <h1 class="welcome-title text-h5 text-sm-h2 mb-0">
      Добро пожаловать в «Класс»
    </h1>

    <p class="welcome-description text-body-1 text-sm-h5 mt-4">
      Организуйте учебный процесс, создавайте задания
      и контролируйте результаты их выполнения.
    </p>

    <div class="d-flex flex-column flex-sm-row justify-center ga-4 mt-6 px-6 px-sm-0 font-weight-bold">
      <router-link
        :to="{ name: 'register' }"
        custom
        v-slot="{ navigate }"
      >
        <v-btn size="x-large"
               color="background"
               variant="tonal"
               class="login-button"
               @click="navigate"
        >
          Зарегистрироваться
        </v-btn>
      </router-link>

      <router-link
        :to="{ name: 'login' }"
        custom
        v-slot="{ navigate }"
      >
        <v-btn
          size="x-large"
          color="background"
          variant="flat"
          class="text-darkprimary font-weight-bold login-button"
          @click="navigate"
        >
          Авторизоваться
        </v-btn>
      </router-link>
    </div>
  </div>
</template>

<style scoped>
.login-button {
  font-size: 20px;
}

.welcome-title {
  font-weight: 700;
}

.welcome-description {
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

@media (min-width: 960px) {
  .welcome-title {
    font-size: 3.5rem !important; /* ~56px */
  }

  .welcome-description {
    font-size: 1.5rem !important; /* ~24px */
  }
}

@media (min-width: 1280px) {
  .welcome-title {
    font-size: 3.5rem !important; /* ~72px */
  }

  .welcome-description {
    font-size: 1.5rem !important; /* ~28px */
  }
}
</style>
