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
    GET_DETAILED_ERROR: 'Не удалось загрузить класс',
    CREATE_SUCCESS: 'Создан новый класс',
    CREATE_ERROR: 'Не удалось создать класс',
    COLORS_ERROR: 'Не удалось загрузить цвета',
    GENERATE_INVITE_ERROR: 'Не удалось сгенерировать код',
    ARCHIVE_ERROR: 'Не удалось архивировать класс',
    ARCHIVE_SUCCESS: 'Архивирован класс',
    UNARCHIVE_ERROR: 'Не удалось разархивировать класс',
    UNARCHIVE_SUCCESS: 'Разархивирован класс',
  },
  CLASSROOM_MEMBERSHIP: {
    FAVOURITE_ERROR: 'Не удалось добавить класс в избранные',
    HIDDEN_ERROR: 'Не удалось скрыть класс',
    LEAVE_ERROR: 'Не удалось покинуть класс',
    LEAVE_SUCCESS: 'Вы покинули класс',
    JOIN_ERROR: 'Не удалось подключиться к классу',
    JOIN_SUCCESS: 'Вы успешно подключились к классу',
  }
} as const
