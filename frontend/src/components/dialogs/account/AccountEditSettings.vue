<script setup lang="ts">
const emit = defineEmits<{
  cancel: []
  saved: []
}>()
</script>

<template>
  <div class="account-settings d-flex flex-column h-100">
    <div class="flex-grow-1 overflow-y-auto px-4">
      <h3 class="text-h6 mb-4">Редактирование аккаунта</h3>
      <!-- форма редактирования -->
    </div>

    <v-toolbar density="compact" color="transparent" class="flex-shrink-0 pa-0 mt-2">
      <v-spacer />
      <v-btn
        variant="tonal"
        size="large"
        rounded="lg"
        class="text-none me-2"
        @click="emit('cancel')"
      >
        Отмена
      </v-btn>
      <v-btn
        color="primary"
        variant="flat"
        size="large"
        rounded="lg"
        class="text-background font-weight-bold text-none me-0"
        prepend-icon="mdi-content-save"
        @click="emit('saved')"
      >
        Сохранить
      </v-btn>
    </v-toolbar>
  </div>
</template>

<style scoped>

</style>
