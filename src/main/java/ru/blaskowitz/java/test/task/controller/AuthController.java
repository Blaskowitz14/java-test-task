package ru.blaskowitz.java.test.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.blaskowitz.java.test.task.dto.response.JwtTokensDto;
import ru.blaskowitz.java.test.task.dto.request.LoginDto;
import ru.blaskowitz.java.test.task.dto.request.RefreshTokenDto;
import ru.blaskowitz.java.test.task.service.AuthService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtTokensDto> signIn(@Valid @RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("/access")
    public ResponseEntity<JwtTokensDto> getNewAccessToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        return ResponseEntity.ok(authService.getNewAccessToken(refreshTokenDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtTokensDto> getNewRefreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        return ResponseEntity.ok(authService.getNewRefreshToken(refreshTokenDto));
    }
}
