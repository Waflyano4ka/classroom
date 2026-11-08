package ru.classroom.dto.classroommembership;

import jakarta.validation.constraints.NotNull;

public class UpdateMembershipFavoriteRequest {

    @NotNull
    private Boolean isFavorite;

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}
