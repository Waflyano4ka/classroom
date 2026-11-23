package ru.classroom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.classroom.dto.ClassroomWithCreatorProjection;
import ru.classroom.entity.ClassroomMembership;

import java.util.Optional;

public interface ClassroomMembershipRepository extends JpaRepository<ClassroomMembership, String> {

    @EntityGraph(attributePaths = {
            "classroom",
            "classroom.color",
            "user",
            "role"
    })
    @Query("""
            SELECT new ru.classroom.dto.ClassroomWithCreatorProjection(cm, creatorUser)
            FROM ClassroomMembership cm
            JOIN cm.classroom c
            LEFT JOIN ClassroomMembership creatorCm
              ON creatorCm.classroom = c
                AND creatorCm.role.code = :creatorRoleCode
            LEFT JOIN creatorCm.user creatorUser
            WHERE cm.user.id = :userId
              AND (:isMyProject = false OR cm.role.code = :creatorRoleCode)
              AND (:isHidden = true OR cm.hidden = false)
              AND (cm.leave = :isLeave)
              AND (cm.blocked = :isBlocked)
              AND (
                  :search = ''
                  OR LOWER(cm.classroom.name) LIKE LOWER(CONCAT('%', :search, '%'))
              )
            """)
    Page<ClassroomWithCreatorProjection> findAllByUserId(
            @Param("userId") String userId,
            @Param("isHidden") boolean isHidden,
            @Param("isMyProject") boolean isMyProject,
            @Param("creatorRoleCode") String creatorRoleCode,
            @Param("isLeave") boolean isLeave,
            @Param("isBlocked") boolean isBlocked,
            @Param("search") String search,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "classroom",
            "classroom.color",
            "user",
            "role"
    })
    @Query("""
        SELECT new ru.classroom.dto.ClassroomWithCreatorProjection(cm, creatorUser)
        FROM ClassroomMembership cm
        JOIN cm.classroom c
        LEFT JOIN ClassroomMembership creatorCm
          ON creatorCm.classroom = c
            AND creatorCm.role.code = :creatorRoleCode
        LEFT JOIN creatorCm.user creatorUser
        WHERE c.id = :classroomId
          AND cm.user.id = :userId
          AND (cm.leave = :isLeave)
          AND (cm.blocked = :isBlocked)
        """)
    Optional<ClassroomWithCreatorProjection> findByClassroomIdAndUserId(
            @Param("classroomId") String classroomId,
            @Param("userId") String userId,
            @Param("creatorRoleCode") String creatorRoleCode,
            @Param("isLeave") boolean isLeave,
            @Param("isBlocked") boolean isBlocked
    );

    Optional<ClassroomMembership> findByClassroomIdAndUserId(String classroomId, String userId);
}
