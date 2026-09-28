package ru.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.classroom.entity.AuthProvider;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
}
