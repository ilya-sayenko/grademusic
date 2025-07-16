package com.grademusic.main.service;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.entity.UserStatistics;
import com.grademusic.main.repository.AlbumStatisticsRepository;
import com.grademusic.main.repository.UserStatisticsRepository;
import com.grademusic.main.service.cache.AlbumStatisticsCache;
import com.grademusic.main.service.cache.UserStatisticsCache;
import com.grademusic.main.service.statistics.StatisticsServiceImpl;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    @Mock
    private AlbumStatisticsRepository albumStatisticsRepository;

    @Mock
    private UserStatisticsRepository userStatisticsRepository;

    @Mock
    private AlbumStatisticsCache albumStatisticsCache;

    @Mock
    private UserStatisticsCache userStatisticsCache;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

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