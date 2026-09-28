package ru.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.classroom.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}
