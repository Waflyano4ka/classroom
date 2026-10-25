import api from './api'
import { setAccessToken } from '@/types/AccessToken.ts'
import { API_PATHS } from '@/constants/apiPaths'
import { useCurrentUserStore } from '@/stores/currentUser'
import type { LoginResponse } from '@/types/Auth.ts'

export async function initializeAuth(): Promise<void> {
  const currentUserStore = useCurrentUserStore()
/*  const showSnackbar = inject<
    (message: string, type?: SnackbarType) => void
  >('showSnackbar')*/
  // todo исправить showSnackbar
  // Восстановление авторизации
  try {
    const response = await api.post<LoginResponse>(
      API_PATHS.AUTH.REFRESH,
    )

    setAccessToken(response.data.token)
  } catch (error) {
    setAccessToken(null)
    useCurrentUserStore().clearCurrentUser()

/*    showSnackbar?.(
      getErrorMessage(error, MESSAGES.AUTH.SESSION_RESTORE_ERROR),
      'error',
    )*/

    return
  }

  // Загрузка данных пользователя
  try {
    await currentUserStore.fetchCurrentUser()
  } catch (error) {
    currentUserStore.clearCurrentUser()

/*    showSnackbar?.(
      getErrorMessage(error, MESSAGES.USER.LOAD_ERROR),
      'error',
    )*/
  }
}
