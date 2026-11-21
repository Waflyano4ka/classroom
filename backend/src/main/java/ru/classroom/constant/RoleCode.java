package ru.classroom.constant;

public enum RoleCode {

    CREATOR("CREATOR"),
    TEACHER("TEACHER"),
    STUDENT("STUDENT");

    private final String code;

    RoleCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
