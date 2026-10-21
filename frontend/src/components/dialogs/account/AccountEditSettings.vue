<script setup lang="ts">
import { ref, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { useCurrentUserStore } from '@/stores/currentUser'
import {emailRule, nameRule, passwordRule, requiredRule, usernameRule} from "@/utils/validation.ts";

const emit = defineEmits<{
  cancel: []
  saved: []
}>()

const { user } = storeToRefs(useCurrentUserStore())

const form = ref({
  lastName: '',
  firstName: '',
  middleName: '',
  username: '',
  email: '',
})

const loading = ref(false)
const formRef = ref()

// Заполняем форму текущими данными
watch(
  user,
  (value) => {
    if (!value) return
    form.value = {
      lastName: value.lastName ?? '',
      firstName: value.firstName ?? '',
      middleName: value.middleName ?? '',
      username: value.username ?? '',
      email: value.email ?? '',
    }
  },
  { immediate: true }
)

async function edit() {
  const { valid } = await formRef.value.validate()
  if (!valid) return

  loading.value = true
  try {
    // await api.patch(...)
    emit('saved')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="account-settings d-flex flex-column h-100">
    <v-form class="flex-grow-1 overflow-y-auto px-2 pt-2 thin-scroll" @submit.prevent="edit">
      <div class="profile-layout mb-1">
        <!-- Аватар: на мобильном сверху, на большом справа -->
        <v-avatar size="216" color="primary" class="profile-avatar">
          <!-- todo сделать возможность отображения аватара -->
        </v-avatar>

        <div class="profile-fields">
          <v-text-field
            :model-value="user?.lastName"
            label="Фамилия"
            variant="outlined"
            color="primary"
            base-color="darkprimary"
            rounded="lg"
            class="mb-1"
            :rules="[nameRule]"
          />

          <v-text-field
            :model-value="user?.firstName"
            label="Имя"
            variant="outlined"
            color="primary"
            base-color="darkprimary"
            rounded="lg"
            class="mb-1"
            :rules="[nameRule]"
          />

          <v-text-field
            :model-value="user?.middleName"
            label="Отчество"
            variant="outlined"
            color="primary"
            base-color="darkprimary"
            rounded="lg"
            :rules="[nameRule]"
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
        class="mb-1"
        :rules="[requiredRule, usernameRule]"
      />
      <v-text-field
        :model-value="user?.email"
        label="Почта"
        variant="outlined"
        color="primary"
        base-color="darkprimary"
        rounded="lg"
        :rules="[requiredRule, emailRule]"
      />
    </v-form>

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
