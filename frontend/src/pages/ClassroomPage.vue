<script setup lang="ts">
import { inject, ref, watch } from 'vue'
import { useDisplay } from 'vuetify'
import { useRoute, useRouter } from 'vue-router'
import { API_PATHS } from "@/constants/apiPaths.ts";
import { MESSAGES } from "@/constants/messages.ts";
import axios from "axios";
import api from "@/services/api.ts";

import type { DetailedClassroom } from "@/types/DetailedClassroom.ts";
import type {SnackbarType} from "@/types/Snackbar.ts";

const { width } = useDisplay()
const activeTab = ref('feed')
const tabs = [
  { title: 'Лента', value: 'feed' },
  { title: 'Задания', value: 'assignments' },
  { title: 'Участники', value: 'participants' },
]

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
      <div class="classroom-header d-flex align-center justify-space-between ms-6 my-4 me-4">
        <h1
          class="font-weight-medium pa-0 ma-0 text-truncate classroom-title"
          :style="{ color: classroom?.textHexCode }"
        >
          {{ classroom?.name }}
        </h1>

        <div class="classroom-actions d-flex align-center ga-2">
          <v-btn
            variant="flat"
            rounded="pill"
            :color="classroom?.textHexCode"
            :style="{ color: classroom?.colorHexCode }"
            size="large"
          >
            Настройки
          </v-btn>

          <v-btn
            variant="flat"
            rounded="pill"
            :color="classroom?.textHexCode"
            :style="{ color: classroom?.colorHexCode }"
            size="large"
            @click="goToHome"
          >
            Назад
          </v-btn>
        </div>
      </div>

<!--      todo уменьшить текст на мобильном экране-->

      <div v-if="width >= 800" class="tabs">
        <v-spacer/>
        <div class="buttons bg-background rounded-desktop px-3 pt-3">
          <v-btn
            v-for="tab in tabs"
            :key="tab.value"
            variant="flat"
            rounded="pill"
            size="large"
            class="tab-btn"
            :color="activeTab === tab.value ? classroom?.textHexCode : classroom?.colorHexCode"
            :style="{
              color: activeTab === tab.value
                ? classroom?.colorHexCode
                : classroom?.textHexCode
            }"
            @click="activeTab = tab.value"
          >
            {{ tab.title }}
          </v-btn>
        </div>
      </div>

      <v-tabs
        v-else
        v-model="activeTab"
        class="mobile-tabs"
        hide-slider
        grow
      >
        <v-tab
          v-for="tab in tabs"
          :key="tab.value"
          :value="tab.value"
          :style="{
            '--tab-bg': activeTab === tab.value
              ? classroom?.textHexCode
              : classroom?.colorHexCode,
            '--tab-text': activeTab === tab.value
              ? classroom?.colorHexCode
              : classroom?.textHexCode
          }"
        >
          {{ tab.title }}
        </v-tab>
      </v-tabs>
    </v-sheet>

    <v-sheet
      class="sheet pa-0"
      :color="classroom?.colorHexCode"
    >
      <v-window v-model="activeTab">
        <v-window-item value="feed">
          Лента
        </v-window-item>

        <v-window-item value="assignments">
          Задания
        </v-window-item>

        <v-window-item value="participants">
          Участники
        </v-window-item>
      </v-window>
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

.tabs {
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

.tab-btn {
  min-width: 120px;
}

.rounded-desktop {
  border-top-left-radius: 16px;
}

.classroom-title {
  font-size: 28px;
  min-width: 0;
}

.classroom-actions .v-btn {
  font-size: 18px;
}

.mobile-tabs {
  border-radius: 0 0 8px 8px;
  overflow: hidden;
}

.mobile-tabs :deep(.v-tab) {
  background-color: var(--tab-bg) !important;
  color: var(--tab-text) !important;
}

.mobile-tabs :deep(.v-tab__content) {
  color: inherit;
}

@media (max-width: 800px) {

  .classroom-header {
    flex-direction: column;
    align-items: flex-start !important;
    gap: 16px;
  }

  .classroom-title {
    font-size: 22px;
    max-width: 100%;
  }

  .classroom-actions {
    width: 100%;
    justify-content: space-between;
  }
}
</style>
