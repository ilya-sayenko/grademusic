package com.grademusic.main.service;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.entity.UserStatistics;
import com.grademusic.main.model.Album;
import com.grademusic.main.model.projection.AlbumStatisticsByGrades;
import com.grademusic.main.model.projection.UserStatisticsByGrades;
import com.grademusic.main.repository.AlbumGradeRepository;
import com.grademusic.main.repository.AlbumStatisticsRepository;
import com.grademusic.main.repository.UserStatisticsRepository;
import com.grademusic.main.service.cache.AlbumCache;
import com.grademusic.main.service.cache.AlbumStatisticsCache;
import com.grademusic.main.service.cache.UserStatisticsCache;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.grademusic.main.config.KafkaConfig.ALBUM_STATISTICS_TOPIC;
import static com.grademusic.main.config.KafkaConfig.USER_STATISTICS_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final AlbumGradeRepository albumGradeRepository;

    private final AlbumStatisticsRepository albumStatisticsRepository;

    private final UserStatisticsRepository userStatisticsRepository;

    private final AlbumStatisticsCache albumStatisticsCache;

    private final UserStatisticsCache userStatisticsCache;

    private final AlbumCache albumCache;

    @Override
    @Transactional
    @KafkaListener(
            topics = ALBUM_STATISTICS_TOPIC,
            containerFactory = "albumStatisticsContainerFactory"
    )
    public void updateAlbumStatistics(String albumId) {
        log.info("Received message for album id={} update statistics", albumId);
        AlbumStatisticsByGrades statisticsByGrades = albumGradeRepository.calculateAlbumStatistics(albumId);
        AlbumStatistics albumStatistics = AlbumStatistics.builder()
                .albumId(albumId)
                .grade(statisticsByGrades.getGrade())
                .countOfGrades(statisticsByGrades.getCountOfGrades())
                .build();
        albumStatistics = albumStatisticsRepository.saveAndFlush(albumStatistics);
        albumStatisticsCache.put(albumStatistics);

        Optional<Album> cachedAlbumOpt = albumCache.findById(albumId);
        if (cachedAlbumOpt.isPresent()) {
            Album cachedAlbum = cachedAlbumOpt.get();
            cachedAlbum.setGrade(statisticsByGrades.getGrade());
            albumCache.put(cachedAlbum);
        }
    }

    @Override
    @Transactional
    @KafkaListener(
            topics = USER_STATISTICS_TOPIC,
            containerFactory = "userStatisticsContainerFactory"
    )
    public void updateUserStatistics(Long userId) {
        log.info("Received message for user id={} update statistics", userId);
        UserStatisticsByGrades statisticsByGrades = albumGradeRepository.calculateUserStatistics(userId);
        UserStatistics userStatistics = UserStatistics.builder()
                    .userId(userId)
                    .averageGrade(statisticsByGrades.getAverageGrade())
                    .countOfGrades(statisticsByGrades.getCountOfGrades())
                    .firstGradeDate(statisticsByGrades.getFirstGradeDate())
                    .lastGradeDate(statisticsByGrades.getLastGradeDate())
                    .build();
        userStatistics = userStatisticsRepository.saveAndFlush(userStatistics);
        userStatisticsCache.put(userStatistics);
    }

    @Override
    public AlbumStatistics findAlbumStatisticsById(String albumId) {
        Optional<AlbumStatistics> cachedStatistics = albumStatisticsCache.findById(albumId);
        if (cachedStatistics.isPresent()) {
            log.info("Getting album statistics id={} from cache", albumId);
            return cachedStatistics.get();
        }
        AlbumStatistics albumStatistics = albumStatisticsRepository.findById(albumId)
                .orElse(AlbumStatistics.builder()
                        .albumId(albumId)
                        .countOfGrades(0L)
                        .grade(0.0)
                        .build());
        albumStatisticsCache.put(albumStatistics);

        return albumStatistics;
    }

    @Override
    public List<AlbumStatistics> findAllAlbumStatisticsById(List<String> albumIds) {
        List<AlbumStatistics> albumStatisticsFromCache = albumStatisticsCache.findAllById(albumIds);
        if (albumStatisticsFromCache.size() == albumIds.size()) {
            return albumStatisticsFromCache;
        }

        List<AlbumStatistics> albumStatisticsFromDatabase = albumStatisticsRepository.findAllById(albumIds);
        for (AlbumStatistics albumStatistics : albumStatisticsFromDatabase) {
            albumStatisticsCache.put(albumStatistics);
        }

        return albumStatisticsFromDatabase;
    }

    @Override
    public UserStatistics findUserStatisticsById(Long userId) {
        Optional<UserStatistics> cachedStatistics = userStatisticsCache.findById(userId);
        if (cachedStatistics.isPresent()) {
            log.info("Getting user statistics id={} from cache", userId);
            return cachedStatistics.get();
        }
        UserStatistics userStatistics = userStatisticsRepository.findById(userId).orElse(
                UserStatistics.builder()
                        .userId(userId)
                        .countOfGrades(0L)
                        .averageGrade(0.0)
                        .build()
        );
        userStatisticsCache.put(userStatistics);

        return userStatistics;
    }
}
