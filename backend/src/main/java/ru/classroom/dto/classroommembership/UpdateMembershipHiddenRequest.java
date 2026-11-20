package ru.classroom.dto.classroommembership;

import jakarta.validation.constraints.NotNull;

public class UpdateMembershipHiddenRequest {

    @NotNull
    private Boolean isHidden;

    public Boolean getHidden() {
        return isHidden;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }
}
