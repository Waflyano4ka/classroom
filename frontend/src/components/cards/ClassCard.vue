<script setup lang="ts">
withDefaults(defineProps<{
  title: string
  subtitle: string
  letter?: string
  headerColor?: string
  textColor?: string
}>(), {
  headerColor: 'grey',
  textColor: 'black',
})
</script>

<template>
  <v-card
    rounded="lg"
    elevation="0"
    class="class-card rounded-lg"
  >
    <!-- Шапка -->
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

      <!-- Аватар -->
      <v-avatar
        color="primary"
        size="64"
        class="card-avatar"
      >
        <span class="text-h3">{{ subtitle.charAt(0) }}</span>
      </v-avatar>
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
  max-width: 360px;
  overflow: visible;
}

.card-avatar {
  position: absolute;
  right: 20px;
  bottom: -32px;
  border: 3px solid rgb(var(--v-theme-background));
}
</style>
