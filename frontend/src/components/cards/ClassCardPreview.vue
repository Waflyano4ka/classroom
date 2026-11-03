<script setup lang="ts">
import { computed } from 'vue'
import ClassroomAvatarCard from "../avatrs/ClassroomAvatarCard.vue"

const props = withDefaults(defineProps<{
  title: string
  lastName?: string | null
  firstName?: string | null
  middleName?: string | null
  username?: string
  headerColor?: string
  textColor?: string
}>(), {
  headerColor: 'grey',
  textColor: 'black',
})

const subtitle = computed(() => {
  const parts = [
    props.lastName?.trim(),
    props.firstName?.trim(),
    props.middleName?.trim(),
  ].filter(Boolean)

  if (parts.length > 0) {
    return parts.join(' ')
  }

  return props.username
})

</script>

<template>
  <v-card
    rounded="lg"
    elevation="0"
    class="class-card rounded-lg"
  >
    <v-sheet
      :color="headerColor"
      class="pa-5 pb-10 position-relative rounded-t-lg"
    >
      <v-card-title
        class="text-h6 font-weight-medium pa-0 text-truncate pe-16"
        :style="{ color: textColor }"
      >
        {{ title }}
      </v-card-title>

      <v-card-subtitle
        class="pa-0 text-truncate pe-16 mt-1"
        :style="{ color: textColor, opacity: 0.85 }"
      >
        {{ subtitle }}
      </v-card-subtitle>

      <classroom-avatar-card
        :last-name="lastName"
        :first-name="firstName"
        :login="username ?? 'unknown'"
      />
    </v-sheet>

    <v-sheet height="100" color="background" />

    <v-card-actions class="px-3 py-2 bg-background rounded-b-lg">
      <v-btn icon variant="text" size="small" color="darkfiltersbackground">
        <v-icon>mdi-bookmark-outline</v-icon>
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
  width: 100%;
  max-width: 360px;
  height: auto;              /* важно: не 100% */
  overflow: visible;
}
</style>
