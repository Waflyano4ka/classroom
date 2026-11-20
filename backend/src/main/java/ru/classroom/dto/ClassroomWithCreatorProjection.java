package ru.classroom.dto;

import ru.classroom.entity.ClassroomMembership;
import ru.classroom.entity.User;

public class ClassroomWithCreatorProjection {

    private final ClassroomMembership membership;
    private final User creator;

    public ClassroomWithCreatorProjection(ClassroomMembership membership, User creator) {
        this.membership = membership;
        this.creator = creator;
    }

    public ClassroomMembership getMembership() {
        return membership;
    }

    public User getCreator() {
        return creator;
    }
}
