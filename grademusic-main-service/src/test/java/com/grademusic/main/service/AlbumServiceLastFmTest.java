package com.grademusic.main.service;

import com.grademusic.main.controller.model.AlbumSearchRequest;
import com.grademusic.main.mapper.AlbumMapper;
import com.grademusic.main.model.Album;
import com.grademusic.main.model.lastfm.AlbumInfoLastFm;
import com.grademusic.main.model.lastfm.AlbumSearchRootLastFm;
import com.grademusic.main.service.cache.AlbumCache;
import com.grademusic.main.service.http.LastFmClient;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceLastFmTest {

    private final AlbumMapper albumMapper = Mappers.getMapper(AlbumMapper.class);

    @Mock
    private LastFmClient lastFmClient;

    @Mock
    private AlbumCache albumCache;

    @InjectMocks
    private AlbumServiceLastFm albumServiceLastFm;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(albumServiceLastFm, "albumMapper", albumMapper);
    }

    @Test
    public void shouldFindAlbumByIdFromCache() {
        String albumId = "123";
        Album cachedAlbum = Instancio.of(Album.class)
                .set(field(Album::getId), albumId)
                .create();
        when(albumCache.findById(albumId)).thenReturn(Optional.of(cachedAlbum));
        Album album = albumServiceLastFm.findAlbumById(albumId);

        verify(lastFmClient, never()).albumGetInfo(anyString());
        assertThat(album)
                .usingRecursiveComparison()
                .isEqualTo(cachedAlbum);
    }

    @Test
    public void shouldFindAlbumByIdFromLastFm() {
        String albumId = "123";
        AlbumInfoLastFm albumInfoLastFm = Instancio.create(AlbumInfoLastFm.class);
        Album albumFromLastFm = albumMapper.fromLastFm(albumInfoLastFm.album());
        when(albumCache.findById(albumId)).thenReturn(Optional.empty());
        when(lastFmClient.albumGetInfo(albumId)).thenReturn(albumInfoLastFm);
        Album album = albumServiceLastFm.findAlbumById(albumId);

        verify(albumCache, atLeastOnce()).findById(eq(albumId));
        verify(albumCache, atLeastOnce()).put(any(Album.class));
        verify(lastFmClient, atLeastOnce()).albumGetInfo(eq(albumId));
        assertThat(albumFromLastFm)
                .usingRecursiveComparison()
                .ignoringFields("grade", "id")
                .isEqualTo(album);
        assertEquals(albumId, album.getId());
    }

    @Test
    public void shouldFindAllAlbumsByIdFromCache() {
        int size = 4;
        List<String> albumIds = Instancio.ofList(String.class).size(size).create();
        List<Album> cachedAlbums = Instancio.ofList(Album.class).size(size).create();
        when(albumCache.findAllById(albumIds)).thenReturn(cachedAlbums);
        albumServiceLastFm.findAllAlbumsById(albumIds);

        verify(albumCache, atLeastOnce()).findAllById(albumIds);
    }

    @Test
    public void shouldFindAllAlbumsByIdFromCacheAndFromLastFm() {
        int searchSize = 2;
        int cacheSize = 4;
        List<String> albumIds = Instancio.ofList(String.class).size(searchSize).create();
        List<Album> cachedAlbums = Instancio.ofList(Album.class).size(cacheSize).create();
        when(albumCache.findAllById(albumIds)).thenReturn(cachedAlbums);
        when(lastFmClient.albumGetInfo(anyString())).thenReturn(Instancio.create(AlbumInfoLastFm.class));
        albumServiceLastFm.findAllAlbumsById(albumIds);

        verify(albumCache, atLeastOnce()).findAllById(albumIds);
        verify(lastFmClient, times(cacheSize - searchSize)).albumGetInfo(anyString());
    }

    @Test
    public void shouldFindAlbumByName() {
        AlbumSearchRequest albumSearchRequest = Instancio.of(AlbumSearchRequest.class)
                .withNullable(field(AlbumSearchRequest::albumIds))
                .create();
        when(lastFmClient.albumSearch(anyString())).thenReturn(Instancio.create(AlbumSearchRootLastFm.class));
        albumServiceLastFm.findAlbums(albumSearchRequest);

        verify(lastFmClient, atLeastOnce()).albumSearch(eq(albumSearchRequest.albumName()));
    }
}