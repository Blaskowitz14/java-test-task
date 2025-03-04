package ru.blaskowitz.java.test.task.service;

import ru.blaskowitz.java.test.task.dto.response.JwtTokensDto;
import ru.blaskowitz.java.test.task.dto.request.LoginDto;
import ru.blaskowitz.java.test.task.dto.request.RefreshTokenDto;

public interface AuthService {

    JwtTokensDto login(LoginDto loginDto);

    JwtTokensDto getNewAccessToken(RefreshTokenDto refreshTokenDto);

    JwtTokensDto getNewRefreshToken(RefreshTokenDto refreshTokenDto);
}
