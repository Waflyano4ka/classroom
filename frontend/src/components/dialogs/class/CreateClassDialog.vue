<script setup lang="ts">
import { shallowRef } from 'vue'
import { classNameRule } from "@/utils/validation.ts";
import ClassCardPreview from "@/components/cards/ClassCardPreview.vue";
import ClassCard from "@/components/cards/ClassCard.vue";
import CustomColorPicker from "@/components/pickers/CustomColorPicker.vue";

const dialog = shallowRef(false)
</script>

<template>
  <div class="text-center create-class-wrapper">
    <v-dialog
      v-model="dialog"
      max-width="1200"
      width="90%"
      content-class="settings-dialog"
    >
      <template #activator="{ props: activatorProps }">
        <v-btn
          variant="flat"
          rounded="pill"
          color="accentbackground"
          size="large"
          class="filter-btn"
          v-bind="activatorProps"
        >
          Создать класс
        </v-btn>
      </template>

      <template #default="{ isActive }">
        <v-card class="pa-4 text-darkprimary" color="background">
          <div class="d-flex align-center justify-space-between mb-2">
            <h1 class="text-h5 font-weight-bold mt-0 mb-0">
              Создать класс
            </h1>

            <v-btn
              icon
              variant="text"
              @click="isActive.value = false"
            >
              <v-icon size="32">mdi-close-circle</v-icon>
            </v-btn>
          </div>

          <v-form ref="formRef" class="flex-grow-1 overflow-y-auto px-2 pt-2 thin-scroll">
            <v-text-field
              label="Название класса"
              variant="outlined"
              color="primary"
              base-color="darkprimary"
              rounded="lg"
              class="mb-1"
              :rules="[classNameRule]"
            />
            <v-text-field
              label="Код подключения"
              variant="outlined"
              color="primary"
              base-color="darkprimary"
              rounded="lg"
              readonly
              class="mb-1"
            />
            <v-row>
              <v-col cols="12" md="6">
                <div class="preview-wrapper pa-3 rounded-lg bg-accentbackground">
                  <class-card-preview
                    class="preview-card"
                    title="Обеспечение качества..."
                    last-name="Лясников"
                    first-name="Антон"
                    middle-name="Олегович"
                    username="alyasnikov"
                    header-color="#593640"
                    text-color="#F3D8DD"
                  />
                </div>
              </v-col>

              <v-col cols="12" md="6">
                <div class="pa-3">
                  <custom-color-picker/>
                </div>
              </v-col>
            </v-row>
          </v-form>

          <v-toolbar density="compact" color="transparent" class="flex-shrink-0 pa-0 mt-2">
            <v-spacer />
            <v-btn
              variant="tonal"
              size="large"
              rounded="lg"
              class="text-none me-2"
            >
              Сбросить
            </v-btn>
            <v-btn
              color="primary"
              variant="flat"
              size="large"
              rounded="lg"
              class="text-background font-weight-bold text-none me-0"\
            >
              Создать класс
            </v-btn>
          </v-toolbar>
        </v-card>
      </template>
    </v-dialog>
  </div>
</template>

<style scoped>
.thin-scroll {
  max-height: 60vh;
  overflow-y: auto;
  line-height: 1.6;
  scrollbar-width: thin;
  scrollbar-color: #356859 transparent;
}

:deep(.settings-dialog) {
  margin: 24px !important;
  height: 90vh !important;
  max-height: 720px !important;
}

:deep(.settings-dialog .v-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.create-class-wrapper {
  width: 100%;
}

.filter-btn {
  min-width: 120px;
}

/* Превью на весь блок */
.preview-wrapper {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-card {
  width: 100%;
  max-width: 360px;
}

@media (max-width: 800px) {
  .create-class-wrapper {
    width: 100%;
  }

  .filter-btn {
    width: 100% !important;
  }
}
</style>
