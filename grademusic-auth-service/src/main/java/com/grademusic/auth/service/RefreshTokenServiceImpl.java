package com.grademusic.auth.service;

import com.grademusic.auth.config.JwtConfig;
import com.grademusic.auth.entity.RefreshToken;
import com.grademusic.auth.entity.User;
import com.grademusic.auth.exception.InvalidRefreshTokenException;
import com.grademusic.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtService jwtService;

    private final JwtConfig jwtConfig;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(User user) {
        String refreshToken = jwtService.generateRefreshToken(user);
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .expireDate(LocalDateTime.now().plus(jwtConfig.getRefreshExpiration()))
                .build();
        return refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .filter(t -> t.getExpireDate().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
    }

    @Override
    @Transactional
    public void deleteRefreshTokenByUserId(long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
