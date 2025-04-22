package com.grademusic.main.service;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.entity.UserStatistics;
import com.grademusic.main.model.projection.AlbumStatisticsByGrades;
import com.grademusic.main.model.projection.UserStatisticsByGrades;
import com.grademusic.main.repository.AlbumGradeRepository;
import com.grademusic.main.repository.AlbumStatisticsRepository;
import com.grademusic.main.repository.UserStatisticsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.grademusic.main.config.KafkaConfig.ALBUM_STATISTICS_TOPIC;
import static com.grademusic.main.config.KafkaConfig.USER_STATISTICS_TOPIC;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final AlbumGradeRepository albumGradeRepository;

    private final AlbumStatisticsRepository albumStatisticsRepository;

    private final UserStatisticsRepository userStatisticsRepository;

    @Override
    @Transactional
    @KafkaListener(
            topics = ALBUM_STATISTICS_TOPIC,
            containerFactory = "albumStatisticsContainerFactory"
    )
    public void updateAlbumStatistics(String albumId) {
        AlbumStatisticsByGrades statisticsByGrades = albumGradeRepository.calculateAlbumStatistics(albumId);
        AlbumStatistics albumStatistics = AlbumStatistics.builder()
                .albumId(albumId)
                .averageGrade(statisticsByGrades.getAverageGrade())
                .countOfGrades(statisticsByGrades.getCountOfGrades())
                .build();
        albumStatisticsRepository.save(albumStatistics);
    }

    @Override
    @KafkaListener(
            topics = USER_STATISTICS_TOPIC,
            containerFactory = "userStatisticsContainerFactory"
    )
    public void updateUserStatistics(Long userId) {
        UserStatisticsByGrades statisticsByGrades = albumGradeRepository.calculateUserStatistics(userId);
        UserStatistics userStatistics = UserStatistics.builder()
                    .userId(userId)
                    .averageGrade(statisticsByGrades.getAverageGrade())
                    .countOfGrades(statisticsByGrades.getCountOfGrades())
                    .firstGradeDate(statisticsByGrades.getFirstGradeDate())
                    .lastGradeDate(statisticsByGrades.getFirstGradeDate())
                    .build();
        userStatisticsRepository.save(userStatistics);
    }

    @Override
    public AlbumStatistics findAlbumStatisticsById(String albumId) {
        return albumStatisticsRepository.findById(albumId)
                .orElse(AlbumStatistics.builder()
                        .albumId(albumId)
                        .countOfGrades(0L)
                        .averageGrade(0.0)
                        .build());
    }

    @Override
    public UserStatistics findUserStatisticsById(Long userId) {
        return userStatisticsRepository.findById(userId)
                .orElse(UserStatistics.builder()
                        .userId(userId)
                        .countOfGrades(0L)
                        .averageGrade(0.0)
                        .build());
    }
}
