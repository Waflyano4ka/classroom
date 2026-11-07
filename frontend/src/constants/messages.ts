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
  CLASSROOM: {
    GET_ERROR: 'Не удалось загрузить классы',
    CREATE_SUCCESS: 'Создан новый класс',
    CREATE_ERROR: 'Не удалось создать класс',
    COLORS_ERROR: 'Не удалось загрузить цвета',
    GENERATE_INVITE_ERROR: 'Не удалось сгенерировать код'
  }
} as const
