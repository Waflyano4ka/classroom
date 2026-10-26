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
    (1, 'SAGE', 'Шалфейный', '#DCE8D5', '#29463E'),
    (2, 'BLUE', 'Голубой', '#DCE8F5', '#294568'),
    (3, 'LAVENDER', 'Лавандовый', '#E8DFF5', '#49365E'),
    (4, 'PEACH', 'Персиковый', '#F7DFD0', '#553D30'),
    (5, 'ROSE', 'Пудрово-розовый', '#F3D8DD', '#593640'),
    (6, 'VANILLA', 'Ванильный', '#F7EDC5', '#51451F'),
    (7, 'MINT', 'Мятный', '#D5EAE2', '#29463E'),
    (8, 'SAND', 'Песочный', '#EBD5C4', '#503A2C')
ON CONFLICT (code) DO NOTHING;