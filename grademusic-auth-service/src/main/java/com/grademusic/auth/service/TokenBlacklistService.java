package com.grademusic.auth.service;

public interface TokenBlacklistService {

    void addToBlacklist(String accessToken, long accessExpirationMs);
}
