package ru.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.classroom.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
