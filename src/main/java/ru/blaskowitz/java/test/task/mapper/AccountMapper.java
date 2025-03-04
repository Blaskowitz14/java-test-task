package ru.blaskowitz.java.test.task.mapper;

import org.mapstruct.Mapper;
import ru.blaskowitz.java.test.task.dto.response.AccountDto;
import ru.blaskowitz.java.test.task.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toAccountDto(Account account);
}
