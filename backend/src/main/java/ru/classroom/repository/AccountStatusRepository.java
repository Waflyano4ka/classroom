package ru.classroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.classroom.entity.AccountStatus;

public interface AccountStatusRepository extends JpaRepository<AccountStatus, Long> {
}
