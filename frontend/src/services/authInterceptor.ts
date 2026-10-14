import axios, {
  type AxiosError,
  type AxiosInstance,
  type InternalAxiosRequestConfig,
} from 'axios'

import {
  API_BASE_URL,
  API_PATHS,
  AUTH_API_PATHS,
} from '@/constants/apiPaths'


import { setAccessToken } from '../types/AccessToken.ts'
import { logoutUser } from './auth.ts'
import type { LoginResponse } from '@/types/Auth.ts'

interface RetryableRequest extends InternalAxiosRequestConfig {
  _retry?: boolean
}

const refreshApi = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  timeout: 300_000,
})

let refreshPromise: Promise<string> | null = null

export function setupAuthInterceptors(api: AxiosInstance): void {
  api.interceptors.response.use(
    (response) => response,
    async (error: AxiosError) => {
      const request = error.config as RetryableRequest | undefined

      if (
        error.response?.status !== 401 ||
        !request ||
        request._retry ||
        AUTH_API_PATHS.some((path) => request.url?.startsWith(path))
      ) {
        return Promise.reject(error)
      }

      request._retry = true

      try {
        refreshPromise ??= refreshApi
          .post<LoginResponse>(API_PATHS.AUTH.REFRESH)
          .then(({ data }) => {
            setAccessToken(data.token)
            return data.token
          })
          .finally(() => {
            refreshPromise = null
          })

        const token = await refreshPromise

        request.headers.Authorization = `Bearer ${token}`

        return api(request)
      } catch (refreshError) {
        try {
          await logoutUser()
        } catch (logoutError) {
          console.error('Не удалось выполнить выход из системы:', logoutError)
        }

        return Promise.reject(refreshError)
      }
    },
  )
}
