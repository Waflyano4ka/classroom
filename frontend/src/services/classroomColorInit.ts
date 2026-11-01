import api from '@/services/api'
import { API_PATHS } from "@/constants/apiPaths.ts";
import type {ClassroomColor} from "@/types/ClassroomColor.ts";

export const getClassroomColors = () => {
  return api.get<ClassroomColor[]>(API_PATHS.CLASSROOMS.COLORS)
}
