package com.grademusic.main.service.cache;

import com.grademusic.main.config.RedisConfig;
import com.grademusic.main.entity.AlbumStatistics;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RBuckets;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AlbumStatisticsCacheRedis implements AlbumStatisticsCache {

    private final RedissonClient redissonClient;

    private final RedisConfig redisConfig;

    private static final String ALBUM_STATISTICS_CACHE = "album-statistics:";

    @Override
    public Optional<AlbumStatistics> findById(String albumId) {
        RBucket<AlbumStatistics> cachedStatistics = redissonClient.getBucket(calculateBucketName(albumId));
        return Optional.ofNullable(cachedStatistics.get());
    }

    @Override
    public List<AlbumStatistics> findAllById(List<String> albumIds) {
        RBuckets cachedStatistics = redissonClient.getBuckets();
        Map<String, AlbumStatistics> cachedStatisticsMap = cachedStatistics.get(calculateBucketNames(albumIds));

        return cachedStatisticsMap.values().stream().toList();
    }

    @Override
    public void put(AlbumStatistics albumStatistics) {
        RBucket<AlbumStatistics> cachedStatistics = redissonClient.getBucket(calculateBucketName(albumStatistics.getAlbumId()));
        cachedStatistics.set(albumStatistics, redisConfig.getCacheExpiration());
    }

    private String calculateBucketName(String albumId) {
        return String.format("%s%s", ALBUM_STATISTICS_CACHE, albumId);
    }

    private String[] calculateBucketNames(List<String> albumIds) {
        return albumIds.stream()
                .map(this::calculateBucketName)
                .toArray(String[]::new);
    }
}
