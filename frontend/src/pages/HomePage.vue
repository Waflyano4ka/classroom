<script setup lang="ts">
import { ref } from 'vue'

const searchQuery = ref('')
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
        @click:append-inner=""
      />

      <div class="filters">
        <div class="checkboxes ms-6">
          <v-checkbox
            label="Избранные"
            color="darkfiltersbackground"
            hide-details
            density="compact"
            class="me-1"
          />
          <v-checkbox
            label="Скрытые"
            color="darkfiltersbackground"
            hide-details
            density="compact"
          />
        </div>

        <div class="buttons bg-background rounded-desktop px-3 pt-3">
          <v-btn
            variant="flat"
            rounded="pill"
            color="accentbackground"
            size="large"
            class="filter-btn"
          >
            Создать класс
          </v-btn>
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
    <v-sheet class="sheet bg-accentbackground" height="200">
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
