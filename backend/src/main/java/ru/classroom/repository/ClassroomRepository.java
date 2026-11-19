package ru.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.classroom.entity.Classroom;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, String> {

    boolean existsByInviteCode(String inviteCode);

    Optional<Classroom> findByInviteCode(String inviteCode);
}
