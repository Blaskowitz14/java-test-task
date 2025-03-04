package ru.blaskowitz.java.test.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.blaskowitz.java.test.task.model.EmailData;

@Repository
public interface EmailRepository extends JpaRepository<EmailData, Long> {
    boolean existsByEmailAndUserIdNot(String email, Long userId);
}
