package com.grademusic.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    // TODO redis

    @Override
    public void addToBlacklist(String accessToken, long accessExpirationMs) {

    }
}
