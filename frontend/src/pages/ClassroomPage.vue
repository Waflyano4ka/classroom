<script setup lang="ts">
import {inject, ref, watch} from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { API_PATHS } from "@/constants/apiPaths.ts";
import { MESSAGES } from "@/constants/messages.ts";
import axios from "axios";
import api from "@/services/api.ts";

import type { DetailedClassroom } from "@/types/DetailedClassroom.ts";
import type {SnackbarType} from "@/types/Snackbar.ts";

const route = useRoute()
const router = useRouter()

const classroom = ref<DetailedClassroom | null>(null)
const loading = ref(false)

const showSnackbar = inject<
  (message: string, type?: SnackbarType) => void
>('showSnackbar')

const goToHome = () => {
  router.push({ name: 'home' })
}

const fetchClassroom = async (classroomId: string) => {
  loading.value = true

  try {
    const response = await api.get<DetailedClassroom>(`${API_PATHS.CLASSROOMS.BASE}/${classroomId}`)
    classroom.value = response.data
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.CLASSROOM.GET_DETAILED_ERROR, 'error'
      )
    } else {
      showSnackbar?.(MESSAGES.CLASSROOM.GET_DETAILED_ERROR, 'error')
    }

    goToHome()
  } finally {
    loading.value = false
  }
}

watch(
  () => route.params.classroomId,
  (classroomId) => {
    if (typeof classroomId === 'string') {
      fetchClassroom(classroomId)
    }
  },
  { immediate: true }
)
</script>

<template>
  <v-container class="page-wrapper py-6" fluid>
    <v-sheet
      class="sheet pa-0"
      :color="classroom?.colorHexCode"
    >
      <h1
        class="font-weight-medium pa-0 text-truncate my-4 mx-6 classroom-title"
        :style="{ color: classroom?.textHexCode }"
      >
        {{ classroom?.name }}
      </h1>
<!--      todo уменьшить текст на мобильном экране-->

      <div class="filters">
        <v-spacer/>
        <div class="buttons bg-background rounded-desktop px-3 pt-3">
          <v-btn
            variant="flat"
            rounded="pill"
            color="accentbackground"
            size="large"
            class="filter-btn"
          >
            Лента
          </v-btn>

          <v-btn
            variant="flat"
            rounded="pill"
            color="accentbackground"
            size="large"
            class="filter-btn"
          >
            Задания
          </v-btn>

          <v-btn
            variant="flat"
            rounded="pill"
            color="accentbackground"
            size="large"
            class="filter-btn"
          >
            Участники
          </v-btn>
        </div>
      </div>
    </v-sheet>
  </v-container>
</template>

<style scoped>
.page-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
  overflow-y: auto;
  min-height: 0;

  scrollbar-width: thin;
  scrollbar-color: #356859 transparent;
}

.sheet {
  width: 90%;
  max-width: 1800px;
  flex-shrink: 0;
  border-radius: 8px;
}

:deep(.checkbox-label .v-label),
:deep(.checkbox-label .v-selection-control__wrapper + .v-label),
:deep(.v-checkbox .v-label) {
  color: rgb(var(--v-theme-darkfiltersbackground)) !important;
  opacity: 1 !important;
  font-size: 18px !important;
}

/* На всякий случай ещё и через переменную */
:deep(.checkbox-label) {
  --v-theme-on-surface: var(--v-theme-darkfiltersbackground);
}

:deep(.filter-btn) {
  color: rgb(var(--v-theme-darkfiltersbackground)) !important;
}

.filters {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  flex-wrap: nowrap;
}

.buttons {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
  font-size: 18px;
  margin-top: 12px;
}

.filter-btn {
  min-width: 120px;
}

.rounded-desktop {
  border-top-left-radius: 16px;
}

@media (max-width: 800px) {
  .filters {
    flex-direction: column;
    align-items: stretch;
    gap: 8px;
  }

  .checkboxes {
    margin-top: 12px;
  }

  .buttons {
    flex-direction: column;
    margin-top: 0;
    gap: 10px;
  }

  .filter-btn {
    width: 100%;
    min-width: 0;
  }

  .rounded-desktop {
    border-top-left-radius: 0;
  }
}
</style>
