package ru.classroom.dto;

public class LocalLoginResult {

    private final String userId;
    private final LocalLoginUserResponse localLoginUserResponse;

    public LocalLoginResult(String userId, LocalLoginUserResponse localLoginUserResponse) {
        this.userId = userId;
        this.localLoginUserResponse = localLoginUserResponse;
    }

    public String getUserId() {
        return userId;
    }

    public LocalLoginUserResponse getLocalLoginUserResponse() {
        return localLoginUserResponse;
    }
}
