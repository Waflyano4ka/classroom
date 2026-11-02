package ru.classroom.dto.classroom;

import java.time.OffsetDateTime;

public class ClassroomCardResponse {

    private final String id;
    private final String name;
    private final String colorHexCode;
    private final String textHexCode;
    private final String permissions;
    private final String lastName;
    private final String firstName;
    private final String middleName;
    private final String username;
    private final boolean archived;

    public ClassroomCardResponse(
            String id,
            String name,
            String colorHexCode,
            String textHexCode,
            String permissions,
            String lastName,
            String firstName,
            String middleName,
            String username,
            boolean archived
    ) {
        this.id = id;
        this.name = name;
        this.colorHexCode = colorHexCode;
        this.textHexCode = textHexCode;
        this.permissions = permissions;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.username = username;
        this.archived = archived;
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

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getUsername() {
        return username;
    }

    public boolean isArchived() {
        return archived;
    }
}
