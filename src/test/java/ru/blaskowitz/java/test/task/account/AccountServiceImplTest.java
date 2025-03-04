package ru.blaskowitz.java.test.task.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import ru.blaskowitz.java.test.task.AbstractInitialization;
import ru.blaskowitz.java.test.task.dto.request.TransferDto;
import ru.blaskowitz.java.test.task.exception.InvalidTransferException;
import ru.blaskowitz.java.test.task.exception.InsufficientFunds;
import ru.blaskowitz.java.test.task.repository.AccountRepository;
import ru.blaskowitz.java.test.task.service.AccountServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountServiceImplTest extends AbstractInitialization {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void transferMoney_success() {
        TransferDto transferDto = new TransferDto(2L, new BigDecimal("30.00"));
        Long fromId = 1L;

        when(accountRepository.selectByUserIdForUpdate(1L))
                .thenReturn(Optional.of(accountFrom));
        when(accountRepository.selectByUserIdForUpdate(2L))
                .thenReturn(Optional.of(accountTo));

        accountService.transferMoney(fromId, transferDto);

        assertEquals(new BigDecimal("70.00"), accountFrom.getBalance());
        assertEquals(new BigDecimal("80.00"), accountTo.getBalance());

        verify(accountRepository, times(1)).save(accountFrom);
        verify(accountRepository, times(1)).save(accountTo);
    }

    @Test
    void transferMoney_sameAccount_throwsException() {
        TransferDto transferDto = new TransferDto(1L, new BigDecimal("10.00"));
        Long fromId = 1L;

        InvalidTransferException exception = assertThrows(InvalidTransferException.class,
                                                          () -> accountService.transferMoney(fromId, transferDto));
        assertEquals("Cannot transfer money to the same account", exception.getMessage());
    }

    @Test
    void transferMoney_insufficientFunds_throwsException() {
        TransferDto transferDto = new TransferDto(2L, new BigDecimal("150.00"));
        Long fromId = 1L;

        when(accountRepository.selectByUserIdForUpdate(1L))
                .thenReturn(Optional.of(accountFrom));
        when(accountRepository.selectByUserIdForUpdate(2L))
                .thenReturn(Optional.of(accountTo));

        InsufficientFunds exception = assertThrows(InsufficientFunds.class,
                                                   () -> accountService.transferMoney(fromId, transferDto));
        assertTrue(exception.getMessage().contains("Not enough money"));
    }
}
