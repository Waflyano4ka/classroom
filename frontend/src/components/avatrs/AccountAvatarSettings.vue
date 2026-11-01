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
  <v-avatar size="216" color="primary" class="profile-avatar">
    <span class="avatar-text">{{ initials }}</span>
  </v-avatar>
</template>

<style scoped>
.profile-avatar {
  flex-shrink: 0;
}

.avatar-text {
  font-size: 72px;
  font-weight: 500;
  line-height: 1;
  user-select: none;
}
</style>
