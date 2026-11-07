<script setup lang="ts">
import { ref, shallowRef, watch, inject , computed } from 'vue'
import { storeToRefs } from 'pinia'
import { classNameRule, inviteCodeRule } from "@/utils/validation.ts";
import { useCurrentUserStore } from '@/stores/currentUser'
import { getInviteCode } from '@/services/classroomInviteCode.ts'
import ClassCardPreview from "@/components/cards/ClassCardPreview.vue";
import CustomColorPicker from "@/components/pickers/CustomColorPicker.vue";
import { MESSAGES } from "@/constants/messages.ts";
import { API_PATHS } from "@/constants/apiPaths.ts";
import api from '@/services/api';
import axios from "axios";


import type {SnackbarType} from "@/types/Snackbar.ts";
import type { ClassroomColor } from "@/types/ClassroomColor.ts";

const currentUserStore = useCurrentUserStore()
const { user } = storeToRefs(currentUserStore)
const dialog = shallowRef(false)
const formRef = ref()
const loading = ref(false)
const loadingCode = ref(false)

const emit = defineEmits<{
  created: []
}>()

const previewUser = computed(() => ({
  lastName: user.value?.lastName ?? null,
  firstName: user.value?.firstName ?? null,
  middleName: user.value?.middleName ?? null,
  username: user.value?.username ?? 'unknown',
}))

const showSnackbar = inject<
  (message: string, type?: SnackbarType) => void
>('showSnackbar')

async function generateInviteCode() {
  loadingCode.value = true
  try {
    form.value.code = await getInviteCode()
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.CLASSROOM.GENERATE_INVITE_ERROR,
        'error'
      )
    } else {
      showSnackbar?.(MESSAGES.CLASSROOM.GENERATE_INVITE_ERROR, 'error')
    }
  } finally {
    loadingCode.value = false
  }
}

const form = ref({
  name: '',
  code: '',
  color: null as ClassroomColor | null,
})

function resetForm() {
  form.value.name = ''
  form.value.color = null
  formRef.value?.resetValidation()

  generateInviteCode()
}

const defaultColor = ref<ClassroomColor | null>(null)
watch(
  () => form.value.color,
  (color) => {
    if (color && !defaultColor.value) {
      defaultColor.value = color
    }
  },
  { immediate: true }
)

watch(dialog, (isOpen) => {
  if (isOpen) {
    generateInviteCode()
  } else {
    form.value.name = ''
    form.value.color = defaultColor.value
    formRef.value?.resetValidation()
  }
})

async function submit() {
  const { valid } = await formRef.value.validate()
  if (!valid || !form.value.color) return

  loading.value = true
  try {
    await api.post(API_PATHS.CLASSROOMS.CREATE, {
      name: form.value.name.trim(),
      colorId: form.value.color.id,
      inviteCode: form.value.code,
    })

    showSnackbar?.(MESSAGES.CLASSROOM.CREATE_SUCCESS, 'success')
    emit('created')
    dialog.value = false
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.CLASSROOM.CREATE_ERROR, 'error'
      )
    } else {
      showSnackbar?.(MESSAGES.CLASSROOM.CREATE_ERROR, 'error')
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="text-center create-class-wrapper">
    <v-dialog
      v-model="dialog"
      max-width="1200"
      width="90%"
      content-class="settings-dialog"
    >
      <template #activator="{ props: activatorProps }">
        <v-btn
          variant="flat"
          rounded="pill"
          color="accentbackground"
          size="large"
          class="filter-btn"
          v-bind="activatorProps"
        >
          Создать класс
        </v-btn>
      </template>

      <template #default="{ isActive }">
        <v-card class="pa-4 text-darkprimary" color="background">
          <div class="d-flex align-center justify-space-between mb-2">
            <h1 class="text-h5 font-weight-bold mt-0 mb-0">
              Создать класс
            </h1>

            <v-btn
              icon
              variant="text"
              @click="isActive.value = false"
            >
              <v-icon size="32">mdi-close-circle</v-icon>
            </v-btn>
          </div>

          <v-form ref="formRef" class="flex-grow-1 overflow-y-auto px-2 pt-2 thin-scroll" @submit.prevent="submit">
            <v-text-field
              v-model="form.name"
              label="Название класса"
              variant="outlined"
              color="primary"
              base-color="darkprimary"
              rounded="lg"
              class="mb-1"
              :rules="[classNameRule]"
            />
            <v-text-field
              v-model="form.code"
              label="Код подключения"
              variant="outlined"
              color="primary"
              base-color="darkprimary"
              rounded="lg"
              readonly
              class="mb-1"
              :loading="loadingCode"
              append-inner-icon="mdi-refresh"
              :rules="[inviteCodeRule]"
              @click:append-inner="generateInviteCode"
            />
            <v-row align="stretch">
              <v-col cols="12" md="6">
                <div class="preview-wrapper pa-3 rounded-lg bg-accentbackground">
                  <class-card-preview
                    class="preview-card"
                    :title="form.name || 'Название класса'"
                    :last-name="previewUser.lastName"
                    :first-name="previewUser.firstName"
                    :middle-name="previewUser.middleName"
                    :username="previewUser.username"
                    :header-color="form.color?.hexCode"
                    :text-color="form.color?.textHexCode"
                  />
                </div>
              </v-col>

              <v-col cols="12" md="6">
                <div class="pa-3">
                  <custom-color-picker v-model="form.color" />
                </div>
              </v-col>
            </v-row>
          </v-form>

          <v-toolbar density="compact" color="transparent" class="flex-shrink-0 pa-0 mt-2">
            <v-spacer />
            <v-btn
              variant="tonal"
              size="large"
              rounded="lg"
              class="text-none me-2"
              @click="resetForm"
            >
              Сбросить
            </v-btn>
            <v-btn
              color="primary"
              variant="flat"
              size="large"
              rounded="lg"
              class="text-background font-weight-bold text-none me-0"
              :loading="loading"
              @click="submit"
            >
              Создать класс
            </v-btn>
          </v-toolbar>
        </v-card>
      </template>
    </v-dialog>
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

.create-class-wrapper {
  width: 100%;
}

.filter-btn {
  min-width: 120px;
}

.preview-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100%;
}

.preview-card {
  width: 100%;
  max-width: 360px;
  flex-grow: 0;
  flex-shrink: 0;
}

@media (max-width: 800px) {
  .create-class-wrapper {
    width: 100%;
  }

  .filter-btn {
    width: 100% !important;
  }
}
</style>
