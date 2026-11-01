<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getClassroomColors } from "@/services/classroomColorInit.ts";

import type { ClassroomColor } from "@/types/ClassroomColor.ts";


const props = defineProps<{
  modelValue?: ClassroomColor | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: ClassroomColor]
}>()

const colors = ref<ClassroomColor[]>([])
const loading = ref(false)

async function loadColors() {
  loading.value = true
  try {
    const { data } = await getClassroomColors()
    colors.value = data

    const firstColor = data[0]
    if (firstColor && !props.modelValue) {
      emit('update:modelValue', firstColor)
    }
  } finally {
    loading.value = false
  }
}

function selectColor(color: ClassroomColor) {
  emit('update:modelValue', color)
}

onMounted(() => {
  loadColors()
})
</script>

<template>
  <div class="color-picker">
    <template v-if="loading">
      <v-progress-circular indeterminate size="24" width="2" />
    </template>

    <template v-else>
      <div
        v-for="color in colors"
        :key="color.id"
        class="color-item"
        :class="{ active: modelValue?.id === color.id }"
        :style="{ backgroundColor: color.hexCode, color: color.textHexCode }"
        @click="selectColor(color)"
      >
        <span class="color-name">{{ color.name }}</span>
      </div>
    </template>
  </div>
</template>

<style scoped>
.color-picker {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  align-items: center;
  min-height: 64px;
}

.color-item {
  min-width: 96px;
  height: 64px;
  padding: 0 16px;
  border-radius: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.15s, box-shadow 0.15s;
  border: 2px solid transparent;
  user-select: none;
}

.color-item:hover {
  transform: scale(1.05);
}

.color-item.active {
  border-color: rgb(var(--v-theme-primary));
  box-shadow: 0 0 0 2px rgb(var(--v-theme-primary));
}

.color-name {
  font-size: 15px;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

@media (max-width: 800px) {
  .color-item {
    min-width: 80px;
    height: 52px;
    padding: 0 12px;
  }

  .color-name {
    font-size: 13px;
  }
}
</style>
