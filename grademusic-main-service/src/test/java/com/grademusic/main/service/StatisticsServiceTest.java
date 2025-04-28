package com.grademusic.main.service;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.entity.UserStatistics;
import com.grademusic.main.model.Album;
import com.grademusic.main.model.projection.AlbumStatisticsByGradesImpl;
import com.grademusic.main.model.projection.UserStatisticsByGradesImpl;
import com.grademusic.main.repository.AlbumGradeRepository;
import com.grademusic.main.repository.AlbumStatisticsRepository;
import com.grademusic.main.repository.UserStatisticsRepository;
import com.grademusic.main.service.cache.AlbumCache;
import com.grademusic.main.service.cache.AlbumStatisticsCache;
import com.grademusic.main.service.cache.UserStatisticsCache;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    @Mock
    private AlbumGradeRepository albumGradeRepository;

    @Mock
    private AlbumStatisticsRepository albumStatisticsRepository;

    @Mock
    private UserStatisticsRepository userStatisticsRepository;

    @Mock
    private AlbumStatisticsCache albumStatisticsCache;

    @Mock
    private UserStatisticsCache userStatisticsCache;

    @Mock
    private AlbumCache albumCache;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Test
    public void shouldUpdateAlbumStatistics() {
        String albumId = "album";
        when(albumGradeRepository.calculateAlbumStatistics(albumId))
                .thenReturn(Instancio.create(AlbumStatisticsByGradesImpl.class));
        when(albumStatisticsRepository.saveAndFlush(any(AlbumStatistics.class)))
                .thenReturn(Instancio.create(AlbumStatistics.class));
        when(albumCache.findById(albumId)).thenReturn(Optional.empty());
        statisticsService.updateAlbumStatistics(albumId);

        verify(albumStatisticsRepository, atLeastOnce()).saveAndFlush(any(AlbumStatistics.class));
        verify(albumStatisticsCache, atLeastOnce()).put(any(AlbumStatistics.class));
    }

    @Test
    public void shouldUpdateAlbumStatisticsForAlbumInCache() {
        String albumId = "album";
        when(albumGradeRepository.calculateAlbumStatistics(albumId))
                .thenReturn(Instancio.create(AlbumStatisticsByGradesImpl.class));
        when(albumStatisticsRepository.saveAndFlush(any(AlbumStatistics.class)))
                .thenReturn(Instancio.create(AlbumStatistics.class));
        when(albumCache.findById(albumId)).thenReturn(Optional.of(Instancio.create(Album.class)));
        statisticsService.updateAlbumStatistics(albumId);

        verify(albumStatisticsRepository, atLeastOnce()).saveAndFlush(any(AlbumStatistics.class));
        verify(albumStatisticsCache, atLeastOnce()).put(any(AlbumStatistics.class));
        verify(albumCache, atLeastOnce()).put(any(Album.class));
    }

    @Test
    public void shouldUpdateUserStatistics() {
        long userId = 1L;
        when(albumGradeRepository.calculateUserStatistics(userId))
                .thenReturn(Instancio.create(UserStatisticsByGradesImpl.class));
        when(userStatisticsRepository.saveAndFlush(any(UserStatistics.class)))
                .thenReturn(Instancio.create(UserStatistics.class));
        statisticsService.updateUserStatistics(userId);

        verify(userStatisticsRepository, atLeastOnce()).saveAndFlush(any(UserStatistics.class));
        verify(userStatisticsCache, atLeastOnce()).put(any(UserStatistics.class));
    }

    @Test
    public void shouldFindAlbumStatisticsByIdFromDatabase() {
        String albumId = "123";
        AlbumStatistics storedStatistics = Instancio.create(AlbumStatistics.class);
        when(albumStatisticsCache.findById(albumId)).thenReturn(Optional.empty());
        when(albumStatisticsRepository.findById(albumId)).thenReturn(Optional.of(storedStatistics));
        AlbumStatistics albumStatistics = statisticsService.findAlbumStatisticsById(albumId);

        verify(albumStatisticsRepository, atLeastOnce()).findById(eq(albumId));
        assertThat(storedStatistics)
                .usingRecursiveComparison()
                .isEqualTo(albumStatistics);
    }

    @Test
    public void shouldFindAlbumStatisticsByIdFromCache() {
        String albumId = "123";
        AlbumStatistics cachedStatistics = Instancio.create(AlbumStatistics.class);
        when(albumStatisticsCache.findById(albumId)).thenReturn(Optional.of(cachedStatistics));
        AlbumStatistics albumStatistics = statisticsService.findAlbumStatisticsById(albumId);

        verify(albumStatisticsRepository, never()).findById(eq(albumId));
        assertThat(cachedStatistics)
                .usingRecursiveComparison()
                .isEqualTo(albumStatistics);
    }

    @Test
    public void shouldFindUserStatisticsByIdFromDatabase() {
        long userId = 1L;
        UserStatistics storedStatistics = Instancio.create(UserStatistics.class);
        when(userStatisticsCache.findById(userId)).thenReturn(Optional.empty());
        when(userStatisticsRepository.findById(userId)).thenReturn(Optional.of(storedStatistics));
        UserStatistics userStatistics = statisticsService.findUserStatisticsById(userId);

        verify(userStatisticsRepository, atLeastOnce()).findById(eq(userId));
        assertThat(storedStatistics)
                .usingRecursiveComparison()
                .isEqualTo(userStatistics);
    }

    @Test
    public void shouldFindUserStatisticsByIdFromCache() {
        long userId = 1L;
        UserStatistics cachedStatistics = Instancio.create(UserStatistics.class);
        when(userStatisticsCache.findById(userId)).thenReturn(Optional.of(cachedStatistics));
        UserStatistics userStatistics = statisticsService.findUserStatisticsById(userId);

        verify(userStatisticsRepository, never()).findById(eq(userId));
        assertThat(cachedStatistics)
                .usingRecursiveComparison()
                .isEqualTo(userStatistics);
    }
}