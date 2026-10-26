CREATE TABLE IF NOT EXISTS classrooms
(
    id           VARCHAR(36) PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  TEXT,
    color_id     BIGINT NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_archived  BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_classrooms_color
    FOREIGN KEY (color_id)
    REFERENCES classroom_colors (id)
);

CREATE TRIGGER trg_classrooms_updated_at
    BEFORE UPDATE ON classrooms
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at();

CREATE TABLE IF NOT EXISTS classroom_memberships
(
    id           VARCHAR(36) PRIMARY KEY,
    classroom_id VARCHAR(36) NOT NULL,
    user_id      VARCHAR(36) NOT NULL,
    role_id      BIGINT NOT NULL,
    joined_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_favorite  BOOLEAN NOT NULL DEFAULT FALSE,
    is_hidden    BOOLEAN NOT NULL DEFAULT FALSE,
    is_blocked   BOOLEAN NOT NULL DEFAULT FALSE,
    is_leave     BOOLEAN NOT NULL DEFAULT FALSE,
    block_reason TEXT,
    blocked_at   TIMESTAMPTZ,

    CONSTRAINT fk_classroom_memberships_classroom
    FOREIGN KEY (classroom_id)
    REFERENCES classrooms (id),

    CONSTRAINT fk_classroom_memberships_role
    FOREIGN KEY (role_id)
    REFERENCES roles (id),

    CONSTRAINT uq_classroom_memberships_classroom_user
    UNIQUE (classroom_id, user_id)
);

CREATE INDEX idx_classroom_memberships_user_id
    ON classroom_memberships (user_id);