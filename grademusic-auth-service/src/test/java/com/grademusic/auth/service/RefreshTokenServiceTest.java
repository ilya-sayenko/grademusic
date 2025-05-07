package com.grademusic.auth.service;

import com.grademusic.auth.config.JwtConfig;
import com.grademusic.auth.entity.RefreshToken;
import com.grademusic.auth.entity.User;
import com.grademusic.auth.exception.InvalidRefreshTokenException;
import com.grademusic.auth.repository.RefreshTokenRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private final JwtConfig jwtConfig = new JwtConfig();

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @BeforeEach
    public void setUp() {
        jwtConfig.setRefreshExpiration(Duration.ofMillis(1000));
        ReflectionTestUtils.setField(refreshTokenService, "jwtConfig", jwtConfig);
    }

    @Test
    public void shouldCreateRefreshToken() {
        User user = Instancio.create(User.class);
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("test");
        refreshTokenService.createRefreshToken(user);

        verify(jwtService, atLeastOnce()).generateRefreshToken(any(User.class));
        verify(refreshTokenRepository, atLeastOnce()).save(any(RefreshToken.class));
    }

    @Test
    public void shouldVerifyRefreshToken() {
        RefreshToken refreshToken = Instancio.of(RefreshToken.class)
                .set(field(RefreshToken::getExpireDate), LocalDateTime.now().plusHours(1))
                .create();
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));

        assertDoesNotThrow(() -> refreshTokenService.verifyRefreshToken(refreshToken.getToken()));
    }

    @Test
    public void shouldThrowWhileVerifyingRefreshToken() {
        RefreshToken refreshToken = Instancio.of(RefreshToken.class)
                .set(field(RefreshToken::getExpireDate), LocalDateTime.now().minusHours(1))
                .create();
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));

        assertThrows(InvalidRefreshTokenException.class,
                () -> refreshTokenService.verifyRefreshToken(refreshToken.getToken()));
    }

    @Test
    public void shouldDeleteRefreshTokenByUserId() {
        long userId = 1L;
        refreshTokenService.deleteRefreshTokenByUserId(userId);

        verify(refreshTokenRepository, atLeastOnce()).deleteByUserId(eq(userId));
    }

    @Test
    public void shouldDeleteRefreshToken() {
        String refreshToken = "test";
        refreshTokenService.deleteRefreshToken(refreshToken);

        verify(refreshTokenRepository, atLeastOnce()).deleteByToken(eq(refreshToken));
    }
}