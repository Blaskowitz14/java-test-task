package ru.blaskowitz.java.test.task.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            String authHeader = request.getHeader("Authorization");
            log.info("Authorization header: {}", authHeader);

            String token = getTokenFromRequest(request);
            if (token == null) {
                log.warn("No JWT token found in request");
            } else {
                boolean valid = jwtProvider.validateToken(token);
                log.info("Token validation result: {}", valid);

                if (!valid) {
                    log.error("Invalid JWT token: {}", token);
                } else {
                    Long userId = jwtProvider.getUserId(token);
                    if (userId == null) {
                        log.error("JWT token is valid, but userId extraction returned null. Token: {}", token);
                    } else {
                        log.info("Setting authentication for user: {}", userId);
                        SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(userId));
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Exception in JwtFilter", ex);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
