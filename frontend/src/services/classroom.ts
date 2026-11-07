import api from '@/services/api'
import { API_PATHS } from '@/constants/apiPaths.ts'

export interface ClassroomCardResponse {
  id: string
  name: string
  colorHexCode: string
  textHexCode: string
  permissions: string
  lastName: string | null
  firstName: string | null
  middleName: string | null
  username: string
  archived: boolean
}

export interface PageResponse<T> {
  content: T[]
  totalPages: number
  totalElements: number
  number: number
  size: number
}

export interface GetClassroomsParams {
  page?: number
  size?: number
  isHidden?: boolean
  isMyProjects?: boolean
}

export const getClassrooms = (params: GetClassroomsParams = {}) => {
  return api.get<PageResponse<ClassroomCardResponse>>(API_PATHS.CLASSROOMS.LIST, {
    params: {
      page: params.page ?? 0,
      size: params.size ?? 15,
      isHidden: params.isHidden ?? false,
      isMyProjects: params.isMyProjects ?? false,
    },
  })
}
