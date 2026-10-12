import api from './api'
import { setAccessToken } from '../types/accessToken'
import { API_PATHS } from '../constants/apiPaths'

export async function logoutUser(): Promise<void> {
  try {
    await api.post(API_PATHS.AUTH.LOGOUT)
  } finally {
    setAccessToken(null)
  }
}
