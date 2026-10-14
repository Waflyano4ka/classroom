import axios, { type InternalAxiosRequestConfig } from 'axios'
import { API_BASE_URL } from '@/constants/apiPaths'
import { getAccessToken } from '../types/AccessToken.ts'
import { setupAuthInterceptors } from './authInterceptor'

const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  timeout: 300_000,
})

api.interceptors.request.use((config) => {
  const token = getAccessToken()

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

setupAuthInterceptors(api)

export default api
