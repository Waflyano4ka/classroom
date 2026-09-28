INSERT INTO account_statuses (id, code, name)
VALUES
    (1, 'ACTIVE_UNVERIFIED', 'Не подтверждён'),
    (2, 'ACTIVE_VERIFIED', 'Активен'),
    (3, 'BLOCKED', 'Заблокирован'),
    (4, 'DISABLED', 'Отключён')
ON CONFLICT (code) DO NOTHING;

INSERT INTO auth_providers (id, code, name)
VALUES
    (1, 'LOCAL', 'Локальная аутентификация')
ON CONFLICT (code) DO NOTHING;