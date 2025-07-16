package com.grademusic.auth.service;

import com.grademusic.auth.controller.model.AuthRequest;
import com.grademusic.auth.controller.model.AuthResponse;
import com.grademusic.auth.controller.model.LogoutRequest;
import com.grademusic.auth.controller.model.RefreshTokenRequest;
import com.grademusic.auth.controller.model.RegisterRequest;
import com.grademusic.auth.entity.RefreshToken;
import com.grademusic.auth.entity.User;
import com.grademusic.auth.exception.InvalidPasswordException;
import com.grademusic.auth.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final TokenBlacklistService tokenBlacklistService;

    @Override
    @Transactional
    public AuthResponse login(AuthRequest authRequest) {
        User user = userService.findByUsername(authRequest.username());

        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            throw new InvalidPasswordException("Password invalid");
        }

        refreshTokenService.deleteRefreshTokenByUserId(user.getId());
        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        User user = User.builder()
                .username(registerRequest.username())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .roles(List.of(Role.ROLE_USER))
                .build();

        userService.create(user);
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.refreshToken());
        String username = jwtService.extractUsername(refreshToken.getToken());
        User user = userService.findByUsername(username);
        refreshTokenService.deleteRefreshTokenByUserId(user.getId());

        String newAccessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        String accessToken = logoutRequest.accessToken();
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(logoutRequest.refreshToken());
        tokenBlacklistService.addToBlacklist(accessToken);
        refreshTokenService.deleteRefreshToken(refreshToken.getToken());
    }
}
