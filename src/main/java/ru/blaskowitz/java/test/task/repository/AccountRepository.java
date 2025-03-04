package ru.blaskowitz.java.test.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.blaskowitz.java.test.task.model.Account;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserId(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.user.id = :id")
    Optional<Account> selectByUserIdForUpdate(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Account a SET a.balance = " +
            "CASE WHEN (a.balance * :increaseMultiplier) > (a.initialBalance * :maxMultiplier) " +
            "THEN (a.initialBalance * :maxMultiplier) ELSE (a.balance * :increaseMultiplier) END")
    int bulkUpdateBalances(@Param("increaseMultiplier") BigDecimal increaseMultiplier,
                           @Param("maxMultiplier") BigDecimal maxMultiplier);
}
