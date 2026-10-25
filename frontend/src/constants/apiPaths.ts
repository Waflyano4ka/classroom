export const API_PATHS = {
  AUTH: {
    REGISTER: '/auth/register',
    LOGIN: '/auth/login',
    LOGOUT: '/auth/logout',
    REFRESH: '/auth/refresh',
    CHANGE_PASSWORD: '/auth/password',
  },
  USERS: {
    ME: '/users/me',
  },
} as const

export const API_BASE_URL = 'http://localhost:8080/api'

export const AUTH_API_PATHS: readonly string[] = [
  API_PATHS.AUTH.REGISTER,
  API_PATHS.AUTH.LOGIN,
  API_PATHS.AUTH.LOGOUT,
  API_PATHS.AUTH.REFRESH,
]
