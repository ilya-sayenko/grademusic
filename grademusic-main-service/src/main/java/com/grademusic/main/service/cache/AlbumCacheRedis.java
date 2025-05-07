package com.grademusic.main.service.cache;

import com.grademusic.main.config.RedisConfig;
import com.grademusic.main.model.Album;
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
public class AlbumCacheRedis implements AlbumCache {

    private final RedissonClient redissonClient;

    private final RedisConfig redisConfig;

    private static final String ALBUMS_CACHE = "albums:";

    @Override
    public Optional<Album> findById(String albumId) {
        RBucket<Album> cachedAlbum = redissonClient.getBucket(calculateBucketName(albumId));
        return Optional.ofNullable(cachedAlbum.get());
    }

    @Override
    public List<Album> findAllById(List<String> albumIds) {
        RBuckets cachedAlbums = redissonClient.getBuckets();
        Map<String, Album> cachedAlbumsMap = cachedAlbums.get(calculateBucketNames(albumIds));

        return cachedAlbumsMap.values().stream().toList();
    }

    @Override
    public void put(Album album) {
        RBucket<Album> cachedAlbum = redissonClient.getBucket(calculateBucketName(album.getId()));
        cachedAlbum.set(album, redisConfig.getCacheExpiration());
    }

    private String calculateBucketName(String albumId) {
        return String.format("%s%s", ALBUMS_CACHE, albumId);
    }

    private String[] calculateBucketNames(List<String> albumIds) {
        return albumIds.stream()
                .map(this::calculateBucketName)
                .toArray(String[]::new);
    }
}
