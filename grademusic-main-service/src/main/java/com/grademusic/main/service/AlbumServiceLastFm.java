package com.grademusic.main.service;

import com.grademusic.main.config.RedisConfig;
import com.grademusic.main.mapper.AlbumMapper;
import com.grademusic.main.model.Album;
import com.grademusic.main.model.lastfm.AlbumSearchRootLastFm;
import com.grademusic.main.service.http.LastFmClient;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumServiceLastFm implements AlbumService {

    private final LastFmClient lastFmClient;

    private final AlbumMapper albumMapper;

    private final RedissonClient redissonClient;

    private final RedisConfig redisConfig;

    private static final String ALBUMS_CACHE = "albums:";

    @Override
    public List<Album> findAlbumsByName(String album) {
        AlbumSearchRootLastFm lastFmResults = lastFmClient.albumSearch(album);

        return albumMapper.fromLastFm(
                lastFmResults.results().albumMatches().albums().stream()
                        .filter(albumLastFm -> !albumLastFm.mbid().isBlank())
                        .toList()
        );
    }

    @Override
    public Album findAlbumById(String id) {
        RBucket<Album> cachedAlbum = redissonClient.getBucket(ALBUMS_CACHE + id);
        if (cachedAlbum.isExists()) {
            return cachedAlbum.get();
        }

        Album album = albumMapper.fromLastFm(lastFmClient.albumGetInfo(id).album());
        album.setId(id);
        cachedAlbum.set(album, redisConfig.getCacheExpiration());

        return album;
    }
}
