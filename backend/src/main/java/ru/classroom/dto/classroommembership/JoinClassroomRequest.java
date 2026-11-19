package ru.classroom.dto.classroommembership;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class JoinClassroomRequest {

    @NotBlank
    @Size(min = 6, max = 6)
    @Pattern(regexp = "^[ABCDEFGHJKLMNPQRSTUVWXYZ23456789]{6}$")
    private String inviteCode;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
