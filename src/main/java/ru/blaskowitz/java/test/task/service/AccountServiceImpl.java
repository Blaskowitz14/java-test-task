package ru.blaskowitz.java.test.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.blaskowitz.java.test.task.dto.request.TransferDto;
import ru.blaskowitz.java.test.task.dto.response.AccountDto;
import ru.blaskowitz.java.test.task.exception.InsufficientFunds;
import ru.blaskowitz.java.test.task.exception.InvalidTransferException;
import ru.blaskowitz.java.test.task.exception.NotFoundException;
import ru.blaskowitz.java.test.task.mapper.AccountMapper;
import ru.blaskowitz.java.test.task.model.Account;
import ru.blaskowitz.java.test.task.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final BigDecimal INCREASE_MULTIPLIER = new BigDecimal("1.1");
    private static final BigDecimal MAX_MULTIPLIER = new BigDecimal("2.07");

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "accounts", key = "#userId")
    public AccountDto getAccountByUserId(Long userId) {
        log.info("Fetching account for userId: {}", userId);
        Account account = findAccountByUserId(userId);
        return accountMapper.toAccountDto(account);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Caching(evict = {
            @CacheEvict(value = "accounts", key = "#fromId"),
            @CacheEvict(value = "accounts", key = "#transferDto.to")
    })
    public void transferMoney(Long fromId, TransferDto transferDto) {
        log.info("Starting transferMoney from account {} to account {} with amount {}",
                 fromId, transferDto.getTo(), transferDto.getAmount());
        if (fromId.equals(transferDto.getTo())) {
            log.error("Invalid transfer attempt: fromId {} equals transferDto.to {}", fromId, transferDto.getTo());
            throw new InvalidTransferException("Cannot transfer money to the same account");
        }

        Long firstId = Math.min(fromId, transferDto.getTo());
        Long secondId = Math.max(fromId, transferDto.getTo());
        Account firstAccount = getAccountOrThrow(accountRepository.selectByUserIdForUpdate(firstId));
        Account secondAccount = getAccountOrThrow(accountRepository.selectByUserIdForUpdate(secondId));
        Account fromAccount = fromId.equals(firstId) ? firstAccount : secondAccount;
        Account toAccount = fromId.equals(firstId) ? secondAccount : firstAccount;

        if (!canDecreaseBalance(transferDto.getAmount(), fromAccount.getBalance())) {
            log.error("Insufficient funds: Account {} balance {} is less than transfer amount {}",
                      fromAccount.getId(), fromAccount.getBalance(), transferDto.getAmount());
            throw new InsufficientFunds("Not enough money on account " + fromAccount.getId());
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(transferDto.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transferDto.getAmount()));

        accountRepository.saveAll(List.of(fromAccount, toAccount));
        log.info("Transfer completed successfully: {} transferred from account {} to account {}",
                 transferDto.getAmount(), fromAccount.getId(), toAccount.getId());
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateBalances() {
        log.info("Starting updateBalances");
        int updated = accountRepository.bulkUpdateBalances(INCREASE_MULTIPLIER, MAX_MULTIPLIER);
        log.info("updateBalances: {} accounts updated", updated);
    }

    private Account findAccountByUserId(Long userId) {
        return getAccountOrThrow(accountRepository.findByUserId(userId));
    }

    private Account getAccountOrThrow(Optional<Account> account) {
        return account.orElseThrow(() -> new NotFoundException("Account not found"));
    }

    private boolean canDecreaseBalance(BigDecimal amount, BigDecimal balance) {
        return balance.compareTo(amount) >= 0;
    }
}
