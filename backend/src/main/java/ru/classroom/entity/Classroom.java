package ru.classroom.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.time.OffsetDateTime;

@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "color_id", nullable = false)
    private ClassroomColor color;

    @Column(name = "invite_code", nullable = false, unique = true, length = 6)
    private String inviteCode;

    @Generated
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Generated
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "is_archived", nullable = false)
    private boolean archived = false;

    protected Classroom() {

    }

    public Classroom(String id, String name, ClassroomColor color, String inviteCode) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.inviteCode = inviteCode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ClassroomColor getColor() {
        return color;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isArchived() {
        return archived;
    }
}
