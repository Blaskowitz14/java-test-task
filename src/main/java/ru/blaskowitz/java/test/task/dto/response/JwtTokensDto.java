package ru.blaskowitz.java.test.task.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokensDto {
    private String accessToken;
    private String refreshToken;
}
