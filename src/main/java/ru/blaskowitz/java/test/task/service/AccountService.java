package ru.blaskowitz.java.test.task.service;

import ru.blaskowitz.java.test.task.dto.request.TransferDto;
import ru.blaskowitz.java.test.task.dto.response.AccountDto;

public interface AccountService {

    AccountDto getAccountByUserId(Long userId);

    void transferMoney(Long fromId, TransferDto transferDto);

    void updateBalances();
}
