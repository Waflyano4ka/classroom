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
    <v-sheet class="sheet bg-accentbackground" height="100">

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
</style>
