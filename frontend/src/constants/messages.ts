export const MESSAGES = {
  AUTH: {
    LOGIN_ERROR: 'Не удалось выполнить вход',
    REGISTER_ERROR: 'Не удалось зарегистрироваться',
    LOGIN_SUCCESS: 'Вход выполнен',
    REGISTER_SUCCESS: 'Регистрация успешно завершена. Теперь войдите в аккаунт.',
    SESSION_RESTORE_ERROR: 'Не удалось восстановить сессию',
    CHANGE_PASSWORD_ERROR: 'Не удалось сменить пароль',
    CHANGE_PASSWORD_SUCCESS: 'Пароль успешно изменён',
  },
  USER: {
    LOAD_ERROR: 'Не удалось загрузить данные аккаунта',
    UPDATE_ERROR: 'Не удалось сохранить изменения',
    UPDATE_SUCCESS: 'Данные обновлены',
  },
} as const
