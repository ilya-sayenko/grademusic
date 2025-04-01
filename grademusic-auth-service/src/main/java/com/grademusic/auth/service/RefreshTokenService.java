package com.grademusic.auth.service;

import com.grademusic.auth.entity.RefreshToken;
import com.grademusic.auth.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String refreshToken);

    void deleteRefreshTokenByUserId(long userId);

    @Transactional
    void deleteRefreshToken(String token);
}
