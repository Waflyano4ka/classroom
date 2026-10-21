<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useCurrentUserStore } from '@/stores/currentUser'

const emit = defineEmits<{
  edit: []
}>()

function formatDate(value?: string | Date | null) {
  if (!value) return '—'
  return new Date(value).toLocaleDateString('ru-RU', {
    day: 'numeric',
    month: 'long',
    year: 'numeric',
  })
}

const { user } = storeToRefs(useCurrentUserStore())
</script>

<template>
  <div class="account-settings d-flex flex-column h-100">
    <div class="flex-grow-1 overflow-y-auto px-2 pt-2 thin-scroll">
      <div class="profile-layout mb-1">
        <!-- Аватар: на мобильном сверху, на большом справа -->
        <v-avatar size="216" color="primary" class="profile-avatar">
          <!-- todo сделать возможность отображения аватара -->
        </v-avatar>

        <!-- Поля -->
        <div class="profile-fields">
          <v-text-field
            :model-value="user?.lastName"
            label="Фамилия"
            variant="outlined"
            color="primary"
            base-color="darkprimary"
            rounded="lg"
            readonly
            class="mb-1"
          />

          <v-text-field
            :model-value="user?.firstName"
            label="Имя"
            variant="outlined"
            color="primary"
            base-color="darkprimary"
            rounded="lg"
            readonly
            class="mb-1"
          />

          <v-text-field
            :model-value="user?.middleName"
            label="Отчество"
            variant="outlined"
            color="primary"
            base-color="darkprimary"
            rounded="lg"
            readonly
          />
        </div>
      </div>
      <v-text-field
        :model-value="user?.username"
        label="Логин"
        variant="outlined"
        color="primary"
        base-color="darkprimary"
        rounded="lg"
        readonly
        class="mb-1"
      />
      <v-text-field
        :model-value="user?.email"
        label="Почта"
        variant="outlined"
        color="primary"
        base-color="darkprimary"
        rounded="lg"
        readonly
        hide-details
      />
      <p class="created-at mt-3 ms-2 mb-0 text-darkprimary">
        Аккаунт создан:
        <span class="font-weight-medium">
          {{ formatDate(user?.createdAt) }}
        </span>
      </p>
    </div>

    <v-toolbar density="compact" color="transparent" class="flex-shrink-0 pa-0 me-0 mt-2">
      <v-spacer />
      <v-btn
        color="primary"
        variant="flat"
        size="large"
        rounded="lg"
        class="text-background font-weight-bold text-none me-0"
        prepend-icon="mdi-pencil"
        @click="emit('edit')"
      >
        Редактировать
      </v-btn>
    </v-toolbar>
  </div>
</template>

<style scoped>
.created-at {
  font-size: 0.8rem;
  opacity: 0.45;
}

.thin-scroll {
  max-height: 60vh;
  overflow-y: auto;
  line-height: 1.6;
  scrollbar-width: thin;
  scrollbar-color: #356859 transparent;
}

.profile-layout {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.profile-avatar {
  flex-shrink: 0;
}

.profile-fields {
  width: 100%;
}

/* Планшет и выше — аватар справа */
@media (min-width: 600px) {
  .profile-layout {
    flex-direction: row-reverse;
    align-items: flex-start;
  }

  .profile-fields {
    flex: 1;
    min-width: 0;
  }
}
</style>
