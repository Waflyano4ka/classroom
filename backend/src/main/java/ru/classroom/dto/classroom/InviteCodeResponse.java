package ru.classroom.dto.classroom;

public class InviteCodeResponse {

    private final String inviteCode;

    public InviteCodeResponse(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getInviteCode() {
        return inviteCode;
    }
}
