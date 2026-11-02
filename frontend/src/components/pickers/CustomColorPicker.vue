<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
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

watch(
  () => props.modelValue,
  (value) => {
    if (!value && colors.value.length > 0) {
      const firstColor = colors.value[0]
      if (firstColor) {
        emit('update:modelValue', firstColor)
      }
    }
  }
)
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
        :style="{
          backgroundColor: modelValue?.id === color.id ? color.textHexCode : color.hexCode,
          color: modelValue?.id === color.id ? color.hexCode : color.textHexCode,
        }"
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
