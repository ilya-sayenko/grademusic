package com.grademusic.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grademusic.auth.entity.User;
import com.grademusic.auth.exception.handler.ErrorResponse;
import com.grademusic.auth.service.JwtService;
import com.grademusic.auth.service.TokenBlacklistService;
import com.grademusic.auth.service.UserService;
import com.grademusic.auth.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserService userService;

    private final TokenBlacklistService tokenBlacklistService;

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

        if (tokenBlacklistService.isBlacklisted(token)) {
            sendError(response, "Token revoked");
            return;
        }

        Long userId = jwtService.extractUserId(token);

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findById(userId);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    user.getPassword(),
                    user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).toList()
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
}
