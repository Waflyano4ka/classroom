package ru.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.classroom.entity.UserCredentials;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String> {
}
