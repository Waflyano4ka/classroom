import api from './api'
import { setAccessToken } from '@/types/accessToken'
import { API_PATHS } from '@/constants/apiPaths'
import type { LoginResponse } from '@/types/auth'

export async function initializeAuth(): Promise<void> {
  try {
    const response = await api.post<LoginResponse>(
      API_PATHS.AUTH.REFRESH,
    )

    setAccessToken(response.data.token)
  } catch {
    setAccessToken(null)
  }
}
