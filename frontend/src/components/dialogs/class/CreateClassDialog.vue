<script setup lang="ts">
import { ref, shallowRef, watch, computed } from 'vue'
import { storeToRefs } from 'pinia'
import { classNameRule } from "@/utils/validation.ts";
import { useCurrentUserStore } from '@/stores/currentUser'
import ClassCardPreview from "@/components/cards/ClassCardPreview.vue";
import CustomColorPicker from "@/components/pickers/CustomColorPicker.vue";

import type { ClassroomColor } from "@/types/ClassroomColor.ts";

const currentUserStore = useCurrentUserStore()
const { user } = storeToRefs(currentUserStore)

const previewUser = computed(() => ({
  lastName: user.value?.lastName ?? null,
  firstName: user.value?.firstName ?? null,
  middleName: user.value?.middleName ?? null,
  username: user.value?.username ?? 'unknown',
}))

const dialog = shallowRef(false)
const formRef = ref()

const form = ref({
  name: '',
  code: '',
  color: null as ClassroomColor | null,
})

function resetForm() {
  form.value = {
    name: '',
    code: '',
    color: null,
  }
  formRef.value?.resetValidation()
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

          <v-form ref="formRef" class="flex-grow-1 overflow-y-auto px-2 pt-2 thin-scroll">
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
              class="text-background font-weight-bold text-none me-0"\
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
  align-items: center;       /* вертикальный центр */
  justify-content: center;   /* горизонтальный центр */
  min-height: 100%;
}

.preview-card {
  width: 100%;
  max-width: 360px;
  flex-grow: 0;              /* не растягивать */
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
