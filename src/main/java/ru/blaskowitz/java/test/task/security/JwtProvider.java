package ru.blaskowitz.java.test.task.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.access-expiration}")
    private long accessExpirationMs;

    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpirationMs;

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private Key key;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    public String generateAccessToken(Long userId) {
        log.debug("Generating access token for user: {}", userId);
        return getJwtBuilder(userId, accessExpirationMs).compact();
    }

    public String generateRefreshToken(Long userId) {
        log.debug("Generating refresh token for user: {}", userId);
        return getJwtBuilder(userId, refreshExpirationMs).compact();
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("Token not supported", e);
        } catch (MalformedJwtException e) {
            log.error("Malformed token", e);
        } catch (SignatureException e) {
            log.error("Signature exception", e);
        } catch (IllegalArgumentException e) {
            log.error("Claims is empty", e);
        }
        return false;
    }

    public Long getUserId(String token) {
        try {
            return jwtParser.parseClaimsJws(token)
                    .getBody()
                    .get("userId", Long.class);
        } catch (Exception e) {
            log.error("Error extracting userId from token", e);
            return null;
        }
    }

    private JwtBuilder getJwtBuilder(Long userId, long expiration) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SIGNATURE_ALGORITHM)
                .claim("userId", userId);
    }
}
