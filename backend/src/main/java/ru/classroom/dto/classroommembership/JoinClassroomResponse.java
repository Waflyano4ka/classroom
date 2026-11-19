package ru.classroom.dto.classroommembership;

public class JoinClassroomResponse {

    private final String name;

    public JoinClassroomResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
