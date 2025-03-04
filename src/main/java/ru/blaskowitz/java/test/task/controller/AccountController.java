package ru.blaskowitz.java.test.task.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.blaskowitz.java.test.task.dto.request.TransferDto;
import ru.blaskowitz.java.test.task.dto.response.AccountDto;
import ru.blaskowitz.java.test.task.service.AccountService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<AccountDto> getMyAccount(@Parameter(hidden = true) @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(accountService.getAccountByUserId(userId));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@Parameter(hidden = true) @AuthenticationPrincipal Long userId,
                                                @Valid @RequestBody TransferDto transferDto) {
        accountService.transferMoney(userId, transferDto);
        return ResponseEntity.ok("transferred");
    }
}
