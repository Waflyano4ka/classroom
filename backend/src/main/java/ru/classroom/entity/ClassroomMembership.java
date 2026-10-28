package ru.classroom.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "classroom_memberships",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_classroom_memberships_classroom_user",
                        columnNames = {"classroom_id", "user_id"}
                )
        }
)
public class ClassroomMembership {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Generated
    @Column(name = "joined_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime joinedAt;

    @Column(name = "is_favorite", nullable = false)
    private boolean favorite = false;

    @Column(name = "is_hidden", nullable = false)
    private boolean hidden = false;

    @Column(name = "is_blocked", nullable = false)
    private boolean blocked = false;

    @Column(name = "is_leave", nullable = false)
    private boolean leave = false;

    @Column(name = "block_reason", columnDefinition = "TEXT")
    private String blockReason;

    @Column(name = "blocked_at")
    private OffsetDateTime blockedAt;

    public ClassroomMembership() {

    }

    public ClassroomMembership(String id, Classroom classroom, User user, Role role) {
        this.id = id;
        this.classroom = classroom;
        this.user = user;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public User getUser() {
        return user;
    }

    public Role getRole() {
        return role;
    }

    public OffsetDateTime getJoinedAt() {
        return joinedAt;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isLeave() {
        return leave;
    }

    public String getBlockReason() {
        return blockReason;
    }

    public OffsetDateTime getBlockedAt() {
        return blockedAt;
    }
}
