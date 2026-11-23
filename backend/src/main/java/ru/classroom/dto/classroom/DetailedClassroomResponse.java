package ru.classroom.dto.classroom;

public class DetailedClassroomResponse {

    private final String memberId;
    private final String name;
    private final String description;
    private final String colorHexCode;
    private final String textHexCode;
    private final String permissions;
    private final String lastName;
    private final String firstName;
    private final String middleName;
    private final String username;
    private final boolean favorite;
    private final boolean hidden;
    private final boolean archived;

    public DetailedClassroomResponse(
            String memberId,
            String name,
            String description,
            String colorHexCode,
            String textHexCode,
            String permissions,
            String lastName,
            String firstName,
            String middleName,
            String username,
            boolean favorite,
            boolean hidden,
            boolean archived
    ) {
        this.memberId = memberId;
        this.name = name;
        this.description = description;
        this.colorHexCode = colorHexCode;
        this.textHexCode = textHexCode;
        this.permissions = permissions;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.username = username;
        this.favorite = favorite;
        this.hidden = hidden;
        this.archived = archived;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isArchived() {
        return archived;
    }
}
