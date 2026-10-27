package ru.classroom.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateCurrentUserRequest {
    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String username;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 100)
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\\s-]+$")
    private String lastName;

    @Size(max = 100)
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\\s-]+$")
    private String firstName;

    @Size(max = 100)
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\\s-]+$")
    private String middleName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
}
