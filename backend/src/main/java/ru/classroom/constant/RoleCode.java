package ru.classroom.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleCode {

    CREATOR("CREATOR"),
    TEACHER("TEACHER"),
    STUDENT("STUDENT");

    private final String code;
}
