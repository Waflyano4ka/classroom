import api from './api'
import { setAccessToken } from '../types/AccessToken.ts'
import { API_PATHS } from '../constants/apiPaths'
import { useCurrentUserStore } from '@/stores/currentUser'

export async function logoutUser(): Promise<void> {
  try {
    await api.post(API_PATHS.AUTH.LOGOUT)
  } finally {
    setAccessToken(null)
    useCurrentUserStore().clearCurrentUser()
  }
}
