<script setup lang="ts">
import {computed, inject, ref} from 'vue'
import ClassroomAvatarCard from "../avatrs/ClassroomAvatarCard.vue"
import { updateClassroomFavorite } from '@/services/classroom'
import { isCreator } from '@/constants/roles'
import axios from "axios";
import { MESSAGES } from "@/constants/messages.ts";

import type { ClassroomCardResponse } from "@/services/classroom.ts";
import type { SnackbarType } from "@/types/Snackbar.ts";

const props = defineProps<{
  item: ClassroomCardResponse
}>()

const headerColor = computed(() => {
  return props.item.archived
    ? 'darkgray'
    : props.item.colorHexCode
})

const textColor = computed(() => {
  return props.item.archived
    ? 'lightgray'
    : props.item.textHexCode
})

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
      :color="headerColor"
      class="pa-5 pb-10 position-relative rounded-t-lg card-header"
      :class="{ 'is-hidden': item.hidden }"
    >
      <v-card-title
        class="text-h6 font-weight-medium pa-0 text-truncate pe-16"
        :style="{ color: textColor }"
      >
        {{ item.name }}
      </v-card-title>

      <v-card-subtitle
        class="pa-0 text-truncate pe-16 mt-1"
        :style="{ color: textColor, opacity: 0.85 }"
      >
        {{ subtitle }}
      </v-card-subtitle>

      <classroom-avatar-card
        class="card-avatar"
        :last-name="item.lastName"
        :first-name="item.firstName"
        :login="item.username ?? 'unknown'"
      />
    </v-sheet>

    <v-sheet
      height="100"
      color="background"
      class="d-flex align-start justify-start pa-4"
    >
      <v-chip
        v-if="item.archived"
        color="secondary"
        variant="tonal"
        prepend-icon="mdi-archive-outline"
      >
        В архиве
      </v-chip>
    </v-sheet>

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

      <v-menu location="bottom end">
        <template #activator="{ props: menuProps }">
          <v-btn
            icon
            variant="text"
            size="small"
            color="darkfiltersbackground"
            v-bind="menuProps"
            @click.stop
          >
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>

        <v-list nav bg-color="background" class="compact-list">
          <v-list-item
            :title="item.hidden ? 'Показать' : 'Скрыть'"
            :prepend-icon="item.hidden ? 'mdi-eye-outline' : 'mdi-eye-off-outline'"
            @click="onHide"
          />
          <v-list-item
            v-if="isCreator(item.permissions)"
            :title="item.archived ? 'Разархивировать' : 'Архивировать'"
            :prepend-icon="item.archived ? 'mdi-archive-arrow-up-outline' : 'mdi-archive-outline'"
            class="text-error"
            @click="onArchive"
          />
          <v-list-item
            v-else
            title="Покинуть"
            prepend-icon="mdi-exit-to-app"
            class="text-error"
            @click="onLeave"
          />
        </v-list>
      </v-menu>
    </v-card-actions>
  </v-card>
</template>

<style scoped>
.class-card {
  max-width: 360px;
  overflow: visible;
}

.card-header.is-hidden::after {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: inherit;
  pointer-events: none;
  background: repeating-linear-gradient(
    -45deg,
    rgba(255, 255, 255, 0.18) 0px,
    rgba(255, 255, 255, 0.18) 8px,
    transparent 8px,
    transparent 16px
  );
}

.card-avatar {
  position: absolute;
  right: 20px;
  bottom: -28px;
  z-index: 2; /* выше полосок */
}

.compact-list :deep(.v-list-item__spacer) {
  width: 16px !important;
}

.compact-list :deep(.v-list-item) {
  min-height: 36px;
  padding-top: 0;
  padding-bottom: 0;
}
</style>
