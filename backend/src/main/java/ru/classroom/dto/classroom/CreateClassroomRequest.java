package ru.classroom.dto.classroom;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateClassroomRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private Long colorId;

    @NotBlank
    @Size(min = 6, max = 6)
    @Pattern(regexp = "^[ABCDEFGHJKLMNPQRSTUVWXYZ23456789]{6}$")
    private String inviteCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
