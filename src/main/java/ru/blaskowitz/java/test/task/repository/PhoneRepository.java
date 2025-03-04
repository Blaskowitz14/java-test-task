package ru.blaskowitz.java.test.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.blaskowitz.java.test.task.model.PhoneData;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneData, Long> {
    boolean existsByPhoneAndUserIdNot(String phone, Long userId);
}
