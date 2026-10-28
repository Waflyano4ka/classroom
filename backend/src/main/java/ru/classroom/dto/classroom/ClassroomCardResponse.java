package ru.classroom.dto.classroom;

import java.time.OffsetDateTime;

public class ClassroomCardResponse {

    private final String id;
    private final String name;
    private final String colorHexCode;
    private final String textHexCode;
    private final boolean archived;
    private final String permissions;

    public ClassroomCardResponse(
            String id,
            String name,
            String colorHexCode,
            String textHexCode,
            boolean archived,
            String permissions
    ) {
        this.id = id;
        this.name = name;
        this.colorHexCode = colorHexCode;
        this.textHexCode = textHexCode;
        this.archived = archived;
        this.permissions = permissions;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColorHexCode() {
        return colorHexCode;
    }

    public String getTextHexCode() {
        return textHexCode;
    }

    public String getPermissions() {
        return permissions;
    }

    public boolean isArchived() {
        return archived;
    }
}
