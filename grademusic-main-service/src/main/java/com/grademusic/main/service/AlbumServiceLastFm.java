package com.grademusic.main.service;

import com.grademusic.main.controller.model.AlbumSearchRequest;
import com.grademusic.main.mapper.AlbumMapper;
import com.grademusic.main.model.Album;
import com.grademusic.main.model.lastfm.AlbumSearchRootLastFm;
import com.grademusic.main.service.cache.AlbumCache;
import com.grademusic.main.service.http.LastFmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlbumServiceLastFm implements AlbumService {

    private final LastFmClient lastFmClient;

    private final AlbumMapper albumMapper;

    private final AlbumCache albumCache;

    @Override
    public List<Album> findAlbums(AlbumSearchRequest albumSearchRequest) {
        String albumName = albumSearchRequest.albumName();
        if (albumName != null && !albumName.isBlank()) {
            return findAlbumsByName(albumName);
        }
        List<String> albumIds = albumSearchRequest.albumIds();
        if (albumIds != null && !albumIds.isEmpty()) {
            return findAllAlbumsById(albumIds);
        }

        return List.of();
    }

    @Override
    public Album findAlbumById(String id) {
        Optional<Album> cachedAlbum = albumCache.findById(id);
        if (cachedAlbum.isPresent()) {
            log.info("Getting album id={} from cache", id);
            return cachedAlbum.get();
        }

        Album album = albumMapper.fromLastFm(lastFmClient.albumGetInfo(id).album());
        album.setId(id);
        albumCache.put(album);

        return album;
    }

    @Override
    public List<Album> findAllAlbumsById(List<String> albumIds) {
        List<Album> cachedAlbums = albumCache.findAllById(albumIds);
        if (cachedAlbums.size() == albumIds.size()) {
            log.info("Getting albums ids={} from cache", albumIds);
            return cachedAlbums;
        }

        Map<String, Album> cachedAlbumsMap = cachedAlbums.stream()
                .collect(Collectors.toMap(Album::getId, Function.identity()));
        List<Album> albums = new ArrayList<>();
        for (String albumId : albumIds) {
            Album album = cachedAlbumsMap.get(albumId);
            if (album == null) {
                log.info("Getting album id={} from LastFm", albumId);
                album = albumMapper.fromLastFm(lastFmClient.albumGetInfo(albumId).album());
                album.setId(albumId);
            }
            albums.add(album);
            albumCache.put(album);
        }

        return albums;
    }

    private List<Album> findAlbumsByName(String album) {
        log.info("Getting album name={} from LastFm", album);
        AlbumSearchRootLastFm lastFmResults = lastFmClient.albumSearch(album);

        return albumMapper.fromLastFm(
                lastFmResults.results().albumMatches().albums().stream()
                        .filter(albumLastFm -> !albumLastFm.mbid().isBlank())
                        .toList()
        );
    }
}
