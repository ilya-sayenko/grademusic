package com.grademusic.auth.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final RedissonClient redissonClient;

    private final JwtService jwtService;

    private static final String BLACKLIST_MAP = "jwt:blacklist";

    @Override
    public void addToBlacklist(String token) {
        Date expiration = jwtService.extractExpiration(token);
        long cacheExpiration = Math.max(1, expiration.getTime() - System.currentTimeMillis());
        RBucket<Boolean> bucket = redissonClient.getBucket(calculateBucketName(token));
        bucket.set(true, Duration.ofMillis(cacheExpiration));
    }

    @Override
    public boolean isBlacklisted(String token) {
        return redissonClient.getBucket(calculateBucketName(token)).isExists();
    }

    @Override
    public void removeFromBlacklist(String token) {
        RMapCache<String, Boolean> blacklist = redissonClient.getMapCache(BLACKLIST_MAP);
        blacklist.remove(token);
    }

    private String calculateBucketName(String token) {
        return String.format("%s:%s", BLACKLIST_MAP, token);
    }
}
