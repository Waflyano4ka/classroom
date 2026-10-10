<script setup lang="ts">
import { ref, shallowRef, computed } from 'vue'

const dialog = shallowRef(false)

const menuItems = ref([
  {
    title: 'Аккаунт',
    subtitle: 'Персональная информация',
    icon: 'mdi-account-outline',
    active: true,
  },
  {
    title: 'Безопасность',
    subtitle: 'Пароль и авторизация',
    icon: 'mdi-shield-outline',
    active: false,
  }
])

const activeItem = computed(() =>
  menuItems.value.find(item => item.active)
)

function setActive(item) {
  menuItems.value.forEach(i => (i.active = false))
  item.active = true
}
</script>

<template>
  <div class="text-center">
    <v-dialog
      v-model="dialog"
      max-width="1200"
      width="90%"
      content-class="settings-dialog"
    >
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none account-button me-3"
          color="background"
          size="large"
          prepend-icon="mdi-account"
          rounded="lg"
          variant="tonal"
          v-bind="activatorProps"
        >
          Профиль
        </v-btn>
      </template>

      <v-card class="pa-4 text-darkprimary" color="background">
        <h1 class="text-h5 font-weight-bold mb-2 mt-0">Настройки</h1>
        <v-row class="flex-nowrap" dense>
          <!-- Левая колонка -->
          <v-col cols="auto" md="4" lg="3">
            <v-list-item
              v-for="item in menuItems"
              :key="item.title"
              :active="item.active"
              :prepend-icon="item.icon"
              rounded="lg"
              class="mb-1 menu-item text-darkprimary"
              :class="{ 'px-2': true }"
              @click="setActive(item)"
            >
              <template #prepend>
                <v-icon :icon="item.icon" />
              </template>

              <v-list-item-title class="font-weight-medium text-body-1 d-none d-sm-block">
                {{ item.title }}
              </v-list-item-title>
              <v-list-item-subtitle class="d-none d-sm-block">
                {{ item.subtitle }}
              </v-list-item-subtitle>
            </v-list-item>
          </v-col>

          <!-- Правый контент -->
          <v-col cols md="8" lg="9" class="d-flex flex-column">
            <div class="d-flex d-sm-none align-center mb-4 flex-shrink-0">
              <div>
                <h3 class="text-h5 font-weight-bold mt-2 mb-0">
                  {{ activeItem?.title }}
                </h3>
              </div>
            </div>

            <div class="content-area d-flex align-center justify-center">
              <span class="text-medium-emphasis">
                Content for {{ activeItem?.title }}
              </span>
            </div>
          </v-col>
        </v-row>
      </v-card>
    </v-dialog>
  </div>
</template>

<style scoped>
.account-button {
  height: 48px;
  font-size: 20px;
}

.content-area {
  flex: 1;
  min-height: 0;
  background-color: #f5f5f5;
  border-radius: 12px;
  overflow-y: auto;
}

:deep(.settings-dialog) {
  margin: 24px !important;
  height: 90vh !important;
  max-height: 720px !important;
}

:deep(.settings-dialog .v-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

:deep(.settings-dialog .v-row) {
  flex: 1;
  min-height: 0;
}

/* Убираем отступ после иконки */
:deep(.menu-item .v-list-item__prepend) {
  margin-inline-end: 0 !important;
}

:deep(.menu-item .v-list-item__spacer) {
  width: 0 !important;
  display: none !important;
}

:deep(.menu-item .v-list-item__content) {
  display: none !important;
}

/* На экранах от 600px */
@media (min-width: 600px) {
  :deep(.menu-item .v-list-item__prepend) {
    margin-inline-end: 12px !important;
  }

  :deep(.menu-item .v-list-item__content) {
    display: flex !important;
    flex-direction: column !important;
    min-width: 0;
    overflow: hidden;
  }

  :deep(.menu-item .v-list-item-title) {
    display: block !important;
    font-size: 1.125rem !important;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  :deep(.menu-item .v-list-item-subtitle) {
    display: block !important;
    font-size: 0.875rem !important;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
</style>
