<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  lastName?: string | null
  firstName?: string | null
  login: string
}>()

const initials = computed(() => {
  const last = props.lastName?.trim()
  const first = props.firstName?.trim()

  if (last && first) {
    return (last.charAt(0) + first.charAt(0)).toUpperCase()
  }

  return (props.login?.trim()?.[0] || '?').toUpperCase()
})
</script>

<template>
  <v-avatar
    color="primary"
    size="64"
    class="card-avatar"
  >
    <span class="text-h3">{{ initials }}</span>
  </v-avatar>
</template>

<style scoped>
.card-avatar {
  position: absolute;
  right: 20px;
  bottom: -32px;
  border: 3px solid rgb(var(--v-theme-background));
}
</style>
