package com.grademusic.main.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grademusic.main.exception.handler.ErrorResponse;
import com.grademusic.main.model.User;
import com.grademusic.main.service.JwtService;
import com.grademusic.main.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final RedissonClient redissonClient;

    private static final String BLACKLIST_MAP = "jwt:blacklist";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith(JwtUtils.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = JwtUtils.extractToken(authHeader);

        if (!jwtService.isValidToken(token)) {
            sendError(response, "Token is invalid");
            return;
        }

        if (isBlacklisted(token)) {
            sendError(response, "Token revoked");
            return;
        }

        Long userId = jwtService.extractUserId(token);

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<String> roles = jwtService.extractRoles(token);
            User user = User.builder()
                    .id(userId)
                    .username(jwtService.extractUsername(token))
                    .roles(roles)
                    .build();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    roles.stream().map(SimpleGrantedAuthority::new).toList()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorResponse errorResponse = ErrorResponse.builder().error(message).build();
        byte[] body = new ObjectMapper().writeValueAsBytes(errorResponse);
        response.getOutputStream().write(body);
    }

    private boolean isBlacklisted(String token) {
        return redissonClient.getBucket(calculateBucketName(token)).isExists();
    }

    private String calculateBucketName(String token) {
        return String.format("%s:%s", BLACKLIST_MAP, token);
    }
}
