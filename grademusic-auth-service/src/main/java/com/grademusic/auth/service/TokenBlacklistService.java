package com.grademusic.auth.service;

public interface TokenBlacklistService {

    void addToBlacklist(String accessToken);

    boolean isBlacklisted(String token);

    void removeFromBlacklist(String token);
}
