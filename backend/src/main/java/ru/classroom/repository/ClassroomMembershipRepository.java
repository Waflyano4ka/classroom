package ru.classroom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.classroom.entity.ClassroomMembership;

public interface ClassroomMembershipRepository extends JpaRepository<ClassroomMembership, String> {

    @EntityGraph(attributePaths = {
            "classroom",
            "classroom.color",
            "user",
            "role"
    })
    @Query("""
            SELECT cm
            FROM ClassroomMembership cm
            WHERE cm.user.id = :userId
              AND (:isMyProject = false OR cm.role.code = :creatorRoleCode)
              AND (:isHidden = true OR cm.hidden = false)
              AND (cm.leave = :isLeave)
            """)
    Page<ClassroomMembership> findAllByUserId(
            @Param("userId") String userId,
            @Param("isHidden") boolean isHidden,
            @Param("isMyProject") boolean isMyProject,
            @Param("creatorRoleCode") String creatorRoleCode,
            @Param("isLeave") boolean isLeave,
            Pageable pageable
    );
}
