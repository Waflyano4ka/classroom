CREATE TABLE IF NOT EXISTS account_statuses
(
    id   BIGINT PRIMARY KEY,
    code VARCHAR(50)  NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS auth_providers
(
    id   BIGINT PRIMARY KEY,
    code VARCHAR(50)  NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id                VARCHAR(36) PRIMARY KEY,
    username          VARCHAR(50)  NOT NULL UNIQUE,
    email             VARCHAR(255) NOT NULL UNIQUE,
    last_name         VARCHAR(100),
    first_name        VARCHAR(100),
    middle_name       VARCHAR(100),
    account_status_id BIGINT       NOT NULL,
    auth_provider_id  BIGINT       NOT NULL,
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_users_account_status
        FOREIGN KEY (account_status_id)
            REFERENCES account_statuses (id),

    CONSTRAINT fk_users_auth_provider
        FOREIGN KEY (auth_provider_id)
            REFERENCES auth_providers (id)
);

CREATE TABLE IF NOT EXISTS user_credentials
(
    user_id       VARCHAR(36) PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_credentials_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);