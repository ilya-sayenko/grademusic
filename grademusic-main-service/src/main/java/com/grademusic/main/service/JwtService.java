package com.grademusic.main.service;

import java.util.Date;
import java.util.List;

public interface JwtService {

    boolean isValidToken(String token);

    Long extractUserId(String token);

    String extractUsername(String token);

    List<String> extractRoles(String token);

    Date extractExpiration(String token);
}
