package com.grademusic.auth.service;

import com.grademusic.auth.entity.User;

import java.util.Date;

public interface JwtService {

    String generateToken(User user);

    String generateRefreshToken(User user);

    boolean isValidToken(String token);

    Long extractUserId(String token);

    String extractUsername(String token);

    Date extractExpiration(String token);
}
