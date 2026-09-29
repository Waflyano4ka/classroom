package ru.classroom.dto;

public class LocalLoginUserResponse {

    private final String token;

    public LocalLoginUserResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
