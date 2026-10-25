<script setup lang="ts">
import {ref, onMounted, watch, inject} from 'vue'
import { storeToRefs } from 'pinia'
import { useCurrentUserUpdateStore } from '@/stores/currentUserUpdate'
import { useCurrentUserStore } from '@/stores/currentUser'
import { API_PATHS } from "@/constants/apiPaths.ts";
import { MESSAGES } from "@/constants/messages.ts";
import { emailRule, nameRule, requiredRule, usernameRule} from "@/utils/validation.ts";
import api from '@/services/api';
import axios from "axios";

import type { CurrentUser } from "@/types/CurrentUser.ts"
import type { SnackbarType } from "@/types/Snackbar.ts";

const emit = defineEmits<{
  cancel: []
  saved: []
}>()

const updateStore = useCurrentUserUpdateStore()
const currentUserStore = useCurrentUserStore()
const { draft } = storeToRefs(updateStore)

const formRef = ref()
const isLoading = ref(false)
const errorMessage = ref('')
const showSnackbar = inject<
  (message: string, type?: SnackbarType) => void
>('showSnackbar')

onMounted(() => {
  updateStore.initDraft()
})

async function edit() {
  const { valid } = await formRef.value.validate()
  if (!valid) return

  isLoading.value = true
  errorMessage.value = ''

  try {
    const response = await api.put<CurrentUser>(
      API_PATHS.USERS.ME,
      draft.value,
    )

    currentUserStore.user = response.data
    showSnackbar?.(MESSAGES.USER.UPDATE_SUCCESS, 'success')
    emit('saved')
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.USER.UPDATE_ERROR, 'error'
      )
    } else {
      showSnackbar?.(MESSAGES.USER.UPDATE_ERROR, 'error')
    }
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="account-settings d-flex flex-column h-100">
    <v-form v-if="draft" ref="formRef" class="flex-grow-1 overflow-y-auto px-2 pt-2 thin-scroll" @submit.prevent="edit">
      <div class="profile-layout mb-1">
        <!-- Аватар: на мобильном сверху, на большом справа -->
        <v-avatar size="216" color="primary" class="profile-avatar">
          <!-- todo сделать возможность отображения аватара -->
        </v-avatar>

        <div class="profile-fields">
          <v-text-field
            v-model="draft.lastName"
            label="Фамилия"
            variant="outlined"
            color="primary"
            base-color="darkprimary"
            rounded="lg"
            class="mb-1"
            :rules="[nameRule]"
          />

          <v-text-field
            v-model="draft.firstName"
            label="Имя"
            variant="outlined"
            color="primary"
            base-color="darkprimary"
            rounded="lg"
            class="mb-1"
            :rules="[nameRule]"
          />

          <v-text-field
            v-model="draft.middleName"
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
        v-model="draft.username"
        label="Логин"
        variant="outlined"
        color="primary"
        base-color="darkprimary"
        rounded="lg"
        class="mb-1"
        :rules="[requiredRule, usernameRule]"
      />
      <v-text-field
        v-model="draft.email"
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
        :loading="isLoading"
        @click="edit"
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
