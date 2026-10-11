package ru.classroom.dto;

import java.time.OffsetDateTime;

public class CurrentUserResponse {

    private final String id;
    private final String username;
    private final String email;
    private final String lastName;
    private final String firstName;
    private final String middleName;
    private final String accountStatusName;
    private final OffsetDateTime createdAt;

    public CurrentUserResponse(
            String id,
            String username,
            String email,
            String lastName,
            String firstName,
            String middleName,
            String accountStatusName,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.accountStatusName = accountStatusName;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
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

    public String getAccountStatusName() {
        return accountStatusName;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
