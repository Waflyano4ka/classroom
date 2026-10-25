<script setup lang="ts">
import { ref, shallowRef, markRaw, computed } from 'vue'
import { storeToRefs } from "pinia";
import { useCurrentUserStore } from '@/stores/currentUser.ts'
import { useCurrentUserUpdateStore } from "@/stores/currentUserUpdate.ts";

import AccountSettings from './AccountSettings.vue'
import AccountEditSettings from './AccountEditSettings.vue'
import SecuritySettings from './SecuritySettings.vue'

const updateStore = useCurrentUserUpdateStore()
const currentUserStore = useCurrentUserStore()
const { isLoading } = storeToRefs(currentUserStore)

const dialog = shallowRef(false)
const isEditingAccount = ref(false)

// Обычные объекты — без component
const menuItems = ref([
  {
    key: 'account',
    title: 'Аккаунт',
    subtitle: 'Персональная информация',
    icon: 'mdi-account-outline',
    active: true,
  },
  {
    key: 'security',
    title: 'Безопасность',
    subtitle: 'Пароль и авторизация',
    icon: 'mdi-shield-outline',
    active: false,
  },
])

// Компоненты вне ref — реактивности нет
const componentsMap: Record<string, unknown> = {
  account: markRaw(AccountSettings),
  security: markRaw(SecuritySettings),
  accountEdit: markRaw(AccountEditSettings),
}

const activeItem = computed(() =>
  menuItems.value.find(item => item.active)
)

const activeComponent = computed(() => {
  if (activeItem.value?.key === 'account' && isEditingAccount.value) {
    return componentsMap.accountEdit
  }
  return activeItem.value
    ? componentsMap[activeItem.value.key]
    : null
})

function setActive(item: typeof menuItems.value[number]) {
  menuItems.value.forEach(i => (i.active = false))
  item.active = true
  isEditingAccount.value = false
  updateStore.clearDraft()
}

function onEdit() {
  updateStore.initDraft()
  isEditingAccount.value = true
}

function onCancel() {
  updateStore.clearDraft()
  isEditingAccount.value = false
}

function onSaved() {
  updateStore.clearDraft()
  isEditingAccount.value = false
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
          :loading="isLoading"
        >
          Профиль
        </v-btn>
      </template>
      <template v-slot:default="{ isActive }">
        <v-card class="pa-4 text-darkprimary" color="background">
          <div class="d-flex align-center justify-space-between mb-2">
            <h1 class="text-h5 font-weight-bold mt-0 mb-0">
              Настройки
            </h1>

            <v-btn
              icon
              variant="text"
              @click="isActive.value = false"
            >
              <v-icon size="32">mdi-close-circle</v-icon>
            </v-btn>
          </div>
          <v-row class="flex-nowrap" density="compact">
            <!-- Левая колонка -->
            <v-col cols="auto" md="4" lg="3">
              <v-list-item
                v-for="item in menuItems"
                :key="item.title"
                :active="item.active"
                rounded="lg"
                class="mb-1 menu-item text-darkprimary"
                :class="{ 'px-2': true }"
                @click="setActive(item)"
              >
                <template #prepend>
                  <v-icon :icon="item.icon" />
                </template>

                <v-list-item-title class="font-weight-medium text-body-1">
                  {{ item.title }}
                </v-list-item-title>
                <v-list-item-subtitle>
                  {{ item.subtitle }}
                </v-list-item-subtitle>
              </v-list-item>
            </v-col>

            <!-- Правый контент -->
            <v-col cols md="8" lg="9" class="d-flex flex-column">
              <div class="mobile-only-title align-center mb-4 flex-shrink-0">
                  <h3 class="text-h5 font-weight-bold mt-2 mb-0">
                    {{ activeItem?.title }}
                  </h3>
              </div>

              <div class="content-area">
                <component
                  :is="activeComponent"
                  v-if="activeComponent"
                  @edit="onEdit"
                  @cancel="onCancel"
                  @saved="onSaved"
                />
              </div>
            </v-col>
          </v-row>
        </v-card>
      </template>
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

/* Заголовок справа — только до 800px */
.mobile-only-title {
  display: none;
}

@media (max-width: 799px) {
  .mobile-only-title {
    display: flex;
  }
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

/* Текст меню — от 800px */
@media (min-width: 800px) {
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
