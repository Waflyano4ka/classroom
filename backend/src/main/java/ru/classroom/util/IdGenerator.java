package ru.classroom.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component
public class IdGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PART_LENGTH = 5;
    private static final int PARTS_COUNT = 3;

    private final SecureRandom random = new SecureRandom();

    /**
     * Генерирует идентификатор объекта вида:
     * xxxxx-xxxxx-xxxxx,
     * где PART_LENGTH определяет длину каждой части,
     * а PARTS_COUNT — количество частей, разделённых символом '-'.
     *
     * @return сгенерированный идентификатор
     */
    public String generate() {
        StringBuilder id = new StringBuilder();

        for (int part = 0; part < PARTS_COUNT; part++) {
            if (part > 0) {
                id.append('-');
            }

            appendRandomPart(id);
        }

        return id.toString();
    }

    private void appendRandomPart(StringBuilder id) {
        for (int i = 0; i < PART_LENGTH; i++) {
            id.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
    }
}
