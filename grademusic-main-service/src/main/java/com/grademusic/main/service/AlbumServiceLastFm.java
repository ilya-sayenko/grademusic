package com.grademusic.main.service;

import com.grademusic.main.controller.model.AlbumSearchRequest;
import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.mapper.AlbumMapper;
import com.grademusic.main.model.Album;
import com.grademusic.main.model.lastfm.AlbumSearchRootLastFm;
import com.grademusic.main.service.cache.AlbumCache;
import com.grademusic.main.service.http.LastFmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumServiceLastFm implements AlbumService {

    private final LastFmClient lastFmClient;

    private final AlbumMapper albumMapper;

    private final StatisticsService statisticsService;

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
        Optional<Album> cachedAlbum = albumCache.findById(id);
        if (cachedAlbum.isPresent()) {
            return cachedAlbum.get();
        }

        Album album = albumMapper.fromLastFm(lastFmClient.albumGetInfo(id).album());
        album.setId(id);
        Double grade = statisticsService.findAlbumStatisticsById(id).getGrade();
        album.setGrade(grade);
        albumCache.put(album);

        return album;
    }

    @Override
    public List<Album> findAllAlbumsById(List<String> albumIds) {
        Map<String, Album> albumsFromCache = albumCache.findAllById(albumIds).stream()
                .collect(Collectors.toMap(Album::getId, Function.identity()));

        Map<String, AlbumStatistics> albumStatisticsMap = new HashMap<>();
        if (albumsFromCache.size() != albumIds.size()) {
            albumStatisticsMap = statisticsService.findAllAlbumStatisticsById(albumIds).stream()
                    .collect(Collectors.toMap(AlbumStatistics::getAlbumId, Function.identity()));
        }

        List<Album> albums = new ArrayList<>();
        for (String albumId : albumIds) {
            Album album = albumsFromCache.get(albumId);
            if (album == null) {
                album = albumMapper.fromLastFm(lastFmClient.albumGetInfo(albumId).album());
                album.setId(albumId);
            }
            Double grade = albumStatisticsMap.getOrDefault(albumId, AlbumStatistics.builder().grade(0.0).build())
                    .getGrade();
            album.setGrade(grade);
            albums.add(album);
            albumCache.put(album);
        }

        return albums;
    }
}
