CREATE TABLE IF NOT EXISTS classroom_colors
(
    id   BIGINT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    hex_code CHAR(7) NOT NULL,
    text_hex_code CHAR(7) NOT NULL,

    CONSTRAINT chk_classroom_colors_hex_code
    CHECK (hex_code ~ '^#[0-9A-Fa-f]{6}$'),

    CONSTRAINT chk_classroom_colors_text_hex_code
    CHECK (text_hex_code ~ '^#[0-9A-Fa-f]{6}$')
);

INSERT INTO classroom_colors (id, code, name, hex_code, text_hex_code)
VALUES
    (1, 'SAGE', 'Шалфейный', '#29463E', '#DCE8D5'),
    (2, 'BLUE', 'Голубой', '#294568', '#DCE8F5'),
    (3, 'LAVENDER', 'Лавандовый', '#49365E', '#E8DFF5'),
    (4, 'PEACH', 'Персиковый', '#553D30', '#F7DFD0'),
    (5, 'ROSE', 'Пудрово-розовый', '#593640', '#F3D8DD'),
    (6, 'VANILLA', 'Ванильный', '#51451F', '#F7EDC5'),
    (7, 'MINT', 'Мятный', '#29463E', '#D5EAE2'),
    (8, 'SAND', 'Песочный', '#503A2C', '#EBD5C4')
ON CONFLICT (code) DO NOTHING;