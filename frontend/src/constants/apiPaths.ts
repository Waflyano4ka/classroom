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
  CLASSROOMS: {
    BASE: 'classrooms',
    LIST: 'classrooms',
    CREATE: 'classrooms/create',
    COLORS: 'classrooms/colors',
    INVITE_CODE: 'classrooms/invite-code',
  },
  CLASSROOM_MEMBERSHIPS: {
    BASE: 'classroom-memberships',
    JOIN: 'classroom-memberships/join'
  }
} as const

export const API_BASE_URL = 'http://localhost:8080/api'

export const AUTH_API_PATHS: readonly string[] = [
  API_PATHS.AUTH.REGISTER,
  API_PATHS.AUTH.LOGIN,
  API_PATHS.AUTH.LOGOUT,
  API_PATHS.AUTH.REFRESH,
]
