<script setup lang="ts">
import { ref, watch, onMounted, computed, inject } from 'vue'
import { useDisplay } from 'vuetify'
import { getClassrooms } from '@/services/classroom'
import { MESSAGES } from "@/constants/messages.ts";
import axios from "axios";
import ClassCard from "@/components/cards/ClassCard.vue";
import CreateClassDialog from "@/components/dialogs/class/CreateClassDialog.vue";

import type { ClassroomCardResponse } from '@/services/classroom'
import type { SnackbarType } from "@/types/Snackbar.ts";

const searchQuery = ref('')
const isMyProjects = ref(false)
const isHidden = ref(false)

const classrooms = ref<ClassroomCardResponse[]>([])
const loading = ref(false)

const page = ref(1)
const totalPages = ref(1)

const showSnackbar = inject<
  (message: string, type?: SnackbarType) => void
>('showSnackbar')

async function loadClassrooms() {
  loading.value = true
  try {
    const { data } = await getClassrooms({
      page: page.value - 1,
      size: 15,
      isHidden: isHidden.value,
      isMyProjects: isMyProjects.value,
      search: searchQuery.value.trim() || undefined,
    })

    classrooms.value = data.content
    totalPages.value = Math.max(data.totalPages, 1)
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.CLASSROOM.GET_ERROR, 'error'
      )
    } else {
      showSnackbar?.(MESSAGES.CLASSROOM.GET_ERROR, 'error')
    }

    classrooms.value = []
    totalPages.value = 1
  } finally {
    loading.value = false
  }
}

const { width } = useDisplay()
const paginationVisible = computed(() => {
  if (width.value < 600) return 1
  if (width.value < 800) return 3
  if (width.value < 1000) return 5
  return 7
})

function onSearch() {
  page.value = 1
  loadClassrooms()
}

watch([page, isMyProjects, isHidden], () => {
  loadClassrooms()
})

onMounted(() => {
  loadClassrooms()
})
</script>

<template>
  <v-container class="page-wrapper py-6" fluid>
    <v-sheet class="sheet bg-accentbackground">
      <v-text-field
        v-model="searchQuery"
        append-inner-icon="mdi-magnify"
        density="compact"
        label="Поиск..."
        variant="solo"
        bg-color="background"
        color="primary"
        class="search-field mt-3 mx-3"
        hide-details
        single-line
        @keyup.enter="onSearch"
        @click:append-inner="onSearch"
      />

      <div class="filters">
        <div class="checkboxes ms-6">
          <v-checkbox
            v-model="isMyProjects"
            label="Мои классы"
            color="darkfiltersbackground"
            hide-details
            density="compact"
            class="me-1"
          />
          <v-checkbox
            v-model="isHidden"
            label="Скрытые"
            color="darkfiltersbackground"
            hide-details
            density="compact"
          />
        </div>

        <div class="buttons bg-background rounded-desktop px-3 pt-3">
          <create-class-dialog @created="loadClassrooms"/>

          <v-btn
            variant="flat"
            rounded="pill"
            size="large"
            color="accentbackground"
            class="filter-btn"
          >
            Подключиться
          </v-btn>
        </div>
      </div>
    </v-sheet>
    <v-sheet class="sheet bg-accentbackground pa-3">
      <div v-if="loading" class="d-flex justify-center py-8">
        <v-progress-circular indeterminate color="primary" />
      </div>

      <div v-else-if="classrooms.length === 0" class="text-center py-8 text-medium-emphasis">
        Классы не найдены! Подключитесь к уже существующему или создайте свой собственный.
      </div>

      <div v-else class="cards-grid">
        <ClassCard
          v-for="item in classrooms"
          :key="item.memberId"
          :item="item"
        />
      </div>

      <div v-if="totalPages > 1" class="d-flex justify-center mt-6">
        <v-pagination
          v-model="page"
          :length="totalPages"
          :total-visible="paginationVisible"
          rounded
          active-color="primary"
          color="darkfiltersbackground"
          density="comfortable"
          show-first-last-page
        />
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

.search-field {
  font-size: 18px;
}


:deep(.v-field) {
  box-shadow: none !important;
}


:deep(.v-field input) {
  font-size: 18px !important;
  color: rgb(var(--v-theme-darkbackground)) !important;
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

.checkboxes {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
  margin-top: 24px;
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

.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
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
