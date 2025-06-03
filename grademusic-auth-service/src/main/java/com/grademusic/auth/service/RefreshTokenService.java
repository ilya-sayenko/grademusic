package com.grademusic.auth.service;

import com.grademusic.auth.entity.RefreshToken;
import com.grademusic.auth.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String refreshToken);

    void deleteRefreshTokenByUserId(long userId);

    void deleteRefreshToken(String token);
}
