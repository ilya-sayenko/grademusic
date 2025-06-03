package com.grademusic.main.utils;

public class JwtUtils {

    public static final String BEARER_PREFIX = "Bearer ";

    public static String extractToken(String authHeader) {
        return authHeader.substring(BEARER_PREFIX.length());
    }
}
