package com.grademusic.auth.service;

import com.grademusic.auth.controller.model.AuthRequest;
import com.grademusic.auth.controller.model.AuthResponse;
import com.grademusic.auth.controller.model.LogoutRequest;
import com.grademusic.auth.controller.model.RefreshTokenRequest;
import com.grademusic.auth.controller.model.RegisterRequest;
import com.grademusic.auth.entity.RefreshToken;
import com.grademusic.auth.entity.User;
import com.grademusic.auth.exception.InvalidPasswordException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void shouldLogin() {
        AuthRequest authRequest = Instancio.create(AuthRequest.class);
        User user = Instancio.create(User.class);
        String accessToken = "test";
        RefreshToken refreshToken = Instancio.create(RefreshToken.class);
        when(userService.findByUsername(eq(authRequest.username()))).thenReturn(user);
        when(passwordEncoder.matches(eq(authRequest.password()), eq(user.getPassword()))).thenReturn(true);
        when(jwtService.generateToken(any(User.class))).thenReturn("test");
        when(refreshTokenService.createRefreshToken(any(User.class))).thenReturn(refreshToken);
        AuthResponse authResponse = authService.login(authRequest);

        verify(refreshTokenService, atLeastOnce()).deleteRefreshTokenByUserId(eq(user.getId()));
        verify(jwtService, atLeastOnce()).generateToken(eq(user));
        verify(refreshTokenService, atLeastOnce()).createRefreshToken(eq(user));
        assertEquals(accessToken, authResponse.accessToken());
        assertEquals(refreshToken.getToken(), authResponse.refreshToken());
    }

    @Test
    public void shouldThrowWhileLoginIfPasswordInvalid() {
        AuthRequest authRequest = Instancio.create(AuthRequest.class);
        User user = Instancio.create(User.class);
        when(userService.findByUsername(eq(authRequest.username()))).thenReturn(user);
        when(passwordEncoder.matches(eq(authRequest.password()), eq(user.getPassword()))).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> authService.login(authRequest));
    }

    @Test
    public void shouldRegister() {
        RegisterRequest registerRequest = Instancio.create(RegisterRequest.class);
        authService.register(registerRequest);

        verify(userService, atLeastOnce()).create(any(User.class));
    }

    @Test
    public void shouldRefresh() {
        User user = Instancio.create(User.class);
        RefreshTokenRequest refreshTokenRequest = Instancio.create(RefreshTokenRequest.class);
        RefreshToken oldRefreshToken = Instancio.create(RefreshToken.class);
        RefreshToken newRefreshToken = Instancio.create(RefreshToken.class);
        String newAccessToken = "test";
        when(refreshTokenService.verifyRefreshToken(eq(refreshTokenRequest.refreshToken()))).thenReturn(oldRefreshToken);
        when(jwtService.extractUsername(eq(oldRefreshToken.getToken()))).thenReturn(user.getUsername());
        when(userService.findByUsername(eq(user.getUsername()))).thenReturn(user);
        when(jwtService.generateToken(eq(user))).thenReturn(newAccessToken);
        when(refreshTokenService.createRefreshToken(eq(user))).thenReturn(newRefreshToken);
        AuthResponse authResponse = authService.refresh(refreshTokenRequest);

        verify(refreshTokenService, atLeastOnce()).deleteRefreshTokenByUserId(eq(user.getId()));
        assertEquals(newAccessToken, authResponse.accessToken());
        assertEquals(newRefreshToken.getToken(), authResponse.refreshToken());
    }

    @Test
    void shouldLogout() {
        LogoutRequest logoutRequest = Instancio.create(LogoutRequest.class);
        RefreshToken oldRefreshToken = Instancio.of(RefreshToken.class)
                .set(field(RefreshToken::getToken), logoutRequest.refreshToken())
                .create();
        when(refreshTokenService.verifyRefreshToken(eq(logoutRequest.refreshToken()))).thenReturn(oldRefreshToken);
        authService.logout(logoutRequest);

        verify(tokenBlacklistService, atLeastOnce()).addToBlacklist(eq(logoutRequest.accessToken()));
        verify(refreshTokenService, atLeastOnce()).deleteRefreshToken(eq(logoutRequest.refreshToken()));
    }
}