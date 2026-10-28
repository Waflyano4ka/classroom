package ru.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.classroom.entity.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, String> {
}
