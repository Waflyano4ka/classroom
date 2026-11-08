<script setup lang="ts">
import {computed, inject, ref} from 'vue'
import ClassroomAvatarCard from "../avatrs/ClassroomAvatarCard.vue"
import { updateClassroomFavorite } from '@/services/classroom'
import axios from "axios";
import { MESSAGES } from "@/constants/messages.ts";

import type { ClassroomCardResponse } from "@/services/classroom.ts";
import type { SnackbarType } from "@/types/Snackbar.ts";

const props = defineProps<{
  item: ClassroomCardResponse
}>()

const isFavorite = ref(props.item.favorite)
const loadingFavorite = ref(false)

const subtitle = computed(() => {
  const parts = [
    props.item.lastName?.trim(),
    props.item.firstName?.trim(),
    props.item.middleName?.trim(),
  ].filter(Boolean)

  if (parts.length > 0) {
    return parts.join(' ')
  }

  return props.item.username
})

const showSnackbar = inject<
  (message: string, type?: SnackbarType) => void
>('showSnackbar')

async function onToggleFavorite() {
  if (loadingFavorite.value) return

  loadingFavorite.value = true
  const nextValue = !isFavorite.value

  try {
    await updateClassroomFavorite(props.item.memberId, nextValue)
    isFavorite.value = nextValue
    props.item.favorite = nextValue
  } catch (error) {
    if (axios.isAxiosError(error)) {
      showSnackbar?.(
        error.response?.data?.message ?? MESSAGES.CLASSROOM_MEMBERSHIP.FAVOURITE_ERROR, 'error'
      )
    } else {
      showSnackbar?.(MESSAGES.CLASSROOM_MEMBERSHIP.FAVOURITE_ERROR, 'error')
    }
  } finally {
    loadingFavorite.value = false
  }
}
</script>

<template>
  <v-card
    rounded="lg"
    elevation="0"
    class="class-card rounded-lg"
  >
    <v-sheet
      :color="item.colorHexCode"
      class="pa-5 pb-10 position-relative rounded-t-lg"
    >
      <v-card-title
        class="text-h6 font-weight-medium pa-0 text-truncate pe-16"
        :style="{ color: item.textHexCode }"
      >
        {{ item.name }}
      </v-card-title>

      <v-card-subtitle
        class="pa-0 text-truncate pe-16 mt-1"
        :style="{ color: item.textHexCode, opacity: 0.85 }"
      >
        {{ subtitle }}
      </v-card-subtitle>

      <classroom-avatar-card
        :last-name="item.lastName"
        :first-name="item.firstName"
        :login="item.username ?? 'unknown'"
      />
    </v-sheet>

    <v-sheet height="100" color="background" />

    <v-card-actions class="px-3 py-2 bg-background rounded-b-lg">
      <v-btn
        icon
        variant="text"
        size="small"
        :color="item.favorite ? 'primary' : 'darkfiltersbackground'"
        @click.stop="onToggleFavorite"
      >
        <v-icon>
          {{ item.favorite ? 'mdi-bookmark' : 'mdi-bookmark-outline' }}
        </v-icon>
      </v-btn>

      <v-spacer />

      <v-btn icon variant="text" size="small" color="darkfiltersbackground">
        <v-icon>mdi-dots-vertical</v-icon>
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<style scoped>
.class-card {
  max-width: 360px;
  overflow: visible;
}
</style>
