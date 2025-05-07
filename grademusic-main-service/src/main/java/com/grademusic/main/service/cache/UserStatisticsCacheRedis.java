package com.grademusic.main.service.cache;

import com.grademusic.main.config.RedisConfig;
import com.grademusic.main.entity.UserStatistics;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserStatisticsCacheRedis implements UserStatisticsCache {

    private final RedissonClient redissonClient;

    private final RedisConfig redisConfig;

    private static final String USER_STATISTICS_CACHE = "user-statistics:";

    @Override
    public Optional<UserStatistics> findById(Long userId) {
        RBucket<UserStatistics> cachedStatistics = redissonClient.getBucket(calculateBucketName(userId));
        return Optional.ofNullable(cachedStatistics.get());
    }

    @Override
    public void put(UserStatistics userStatistics) {
        RBucket<UserStatistics> cachedStatistics = redissonClient.getBucket(calculateBucketName(userStatistics.getUserId()));
        cachedStatistics.set(userStatistics, redisConfig.getCacheExpiration());
    }

    private String calculateBucketName(Long userId) {
        return String.format("%s%s", USER_STATISTICS_CACHE, userId);
    }
}
