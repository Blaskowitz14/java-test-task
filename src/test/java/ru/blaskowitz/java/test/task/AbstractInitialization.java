package ru.blaskowitz.java.test.task;

import org.junit.jupiter.api.BeforeEach;
import ru.blaskowitz.java.test.task.dto.request.LoginDto;
import ru.blaskowitz.java.test.task.model.Account;
import ru.blaskowitz.java.test.task.model.PhoneData;
import ru.blaskowitz.java.test.task.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public abstract class AbstractInitialization {

    protected User testUser1;
    protected User testUser2;
    protected Account accountFrom;
    protected Account accountTo;
    protected LoginDto loginDto;

    @BeforeEach
    public void initEntities() {
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setName("Test User One");
        testUser1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testUser1.setPassword("$2a$12$vUvFs7xcQmbAY9D5rQ0vt.70Q1YsmNEmke3UEOF1tsiXddn3jENNe");

        accountFrom = new Account();
        accountFrom.setId(1L);
        accountFrom.setBalance(new BigDecimal("100.00"));
        accountFrom.setInitialBalance(new BigDecimal("100.00"));
        accountFrom.setUser(testUser1);
        testUser1.setAccount(accountFrom);

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setName("Test User Two");
        testUser2.setDateOfBirth(LocalDate.of(1992, 2, 2));
        testUser2.setPassword("hashedPassword2");

        accountTo = new Account();
        accountTo.setId(2L);
        accountTo.setBalance(new BigDecimal("50.00"));
        accountTo.setInitialBalance(new BigDecimal("50.00"));
        accountTo.setUser(testUser2);
        testUser2.setAccount(accountTo);

        loginDto = new LoginDto(
                "79671087514",
                "1234");
    }

}

