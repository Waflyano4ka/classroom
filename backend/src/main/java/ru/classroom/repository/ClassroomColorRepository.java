package ru.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.classroom.entity.ClassroomColor;

import java.util.List;

public interface ClassroomColorRepository extends JpaRepository<ClassroomColor, Long> {
    List<ClassroomColor> findAllByOrderByIdAsc();
}
