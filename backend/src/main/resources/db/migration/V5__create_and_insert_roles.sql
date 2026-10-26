CREATE TABLE IF NOT EXISTS roles
(
    id   BIGINT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);

INSERT INTO roles (id, code, name)
VALUES
    (1, 'CREATOR', 'Создатель'),
    (2, 'TEACHER', 'Преподаватель'),
    (3, 'STUDENT', 'Ученик')
ON CONFLICT (code) DO NOTHING;