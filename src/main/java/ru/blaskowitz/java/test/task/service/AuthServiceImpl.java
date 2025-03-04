package ru.blaskowitz.java.test.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.blaskowitz.java.test.task.dto.response.JwtTokensDto;
import ru.blaskowitz.java.test.task.dto.request.LoginDto;
import ru.blaskowitz.java.test.task.dto.request.RefreshTokenDto;
import ru.blaskowitz.java.test.task.exception.AuthenticationException;
import ru.blaskowitz.java.test.task.model.User;
import ru.blaskowitz.java.test.task.security.JwtProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtTokensDto login(LoginDto loginDto) {
        log.info("User login attempt for login: {}", loginDto.getLogin());
        User user = userService.findByEmailOrPhone(loginDto.getLogin());
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            log.error("Authentication failed for user {}: wrong password", loginDto.getLogin());
            throw new AuthenticationException("Wrong password");
        }
        log.info("User {} authenticated successfully", user.getId());
        return JwtTokensDto.builder()
                .accessToken(jwtProvider.generateAccessToken(user.getId()))
                .refreshToken(jwtProvider.generateRefreshToken(user.getId()))
                .build();
    }

    @Override
    public JwtTokensDto getNewAccessToken(RefreshTokenDto refreshTokenDto) {
        log.info("Generating new access token");
        String refreshToken = refreshTokenDto.getToken();
        validateToken(refreshToken);
        Long userId = jwtProvider.getUserId(refreshToken);
        log.info("New access token generated for user {}", userId);
        return JwtTokensDto.builder()
                .accessToken(jwtProvider.generateAccessToken(userId))
                .build();
    }

    @Override
    public JwtTokensDto getNewRefreshToken(RefreshTokenDto refreshTokenDto) {
        log.info("Generating new refresh token");
        String refreshToken = refreshTokenDto.getToken();
        validateToken(refreshToken);
        Long userId = jwtProvider.getUserId(refreshToken);
        log.info("New tokens generated for user {}", userId);
        return JwtTokensDto.builder()
                .accessToken(jwtProvider.generateAccessToken(userId))
                .refreshToken(jwtProvider.generateRefreshToken(userId))
                .build();
    }

    private void validateToken(String token) {
        if (!jwtProvider.validateToken(token)) {
            log.error("Invalid refresh token provided");
            throw new AuthenticationException("Invalid token");
        }
    }
}

