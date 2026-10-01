// Обязательное поле
export const requiredRule = (value: string) =>
  !!value || 'Поле обязательно для заполнения'


// Валидация пароля на наличие определенных цифр и кол-ва символов
export const passwordRule = (value: string) => {
  if (value.length < 8 || value.length > 25) {
    return 'Пароль должен содержать от 8 до 25 символов'
  }

  if (!/[a-zA-Z]/.test(value)) {
    return 'Пароль должен содержать латинскую букву'
  }

  if (!/\d/.test(value)) {
    return 'Пароль должен содержать цифру'
  }

  if (!/[^\w\s]/.test(value)) {
    return 'Пароль должен содержать специальный символ'
  }

  if (!/^[a-zA-Z0-9\p{P}\p{S}]+$/u.test(value)) {
    return 'Пароль содержит недопустимые символы'
  }

  return true
}

// Проверка "Второго" пароля
export const passwordConfirmationRule = (
  value: string,
  password: string,
) =>
  value === password || 'Пароли не совпадают'

// Валидация логина
export const usernameRule = (value: string) => {
  if (value.length > 20) {
    return 'Логин должен содержать не более 20 символов'
  }

  if (!/^[a-zA-Z0-9]+$/.test(value)) {
    return 'Логин может содержать только латинские буквы и цифры'
  }

  return true
}

// Валидация почты
export const emailRule = (value: string) =>
  /.+@.+\..+/.test(value) || 'Введите корректный адрес электронной почты'

// Пользовательское соглашение
export const agreementRule = (value: boolean) =>
  value || 'Необходимо согласиться с условиями пользовательского соглашения'
