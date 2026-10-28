package ru.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.classroom.entity.ClassroomMembership;

public interface ClassroomMembershipRepository extends JpaRepository<ClassroomMembership, String> {
}
