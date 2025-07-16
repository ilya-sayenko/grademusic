package com.grademusic.main.service.statistics;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.entity.UserStatistics;
import com.grademusic.main.model.message.AlbumStatisticsUpdateMessage;
import com.grademusic.main.model.StatisticsType;
import com.grademusic.main.model.message.UserStatisticsUpdateMessage;
import com.grademusic.main.repository.AlbumStatisticsRepository;
import com.grademusic.main.repository.UserStatisticsRepository;
import com.grademusic.main.service.cache.AlbumStatisticsCache;
import com.grademusic.main.service.cache.UserStatisticsCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.grademusic.main.config.KafkaConfig.ALBUM_STATISTICS_TOPIC;
import static com.grademusic.main.config.KafkaConfig.USER_STATISTICS_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final AlbumStatisticsRepository albumStatisticsRepository;

    private final UserStatisticsRepository userStatisticsRepository;

    private final AlbumStatisticsCache albumStatisticsCache;

    private final UserStatisticsCache userStatisticsCache;

    private final UserStatisticsCalculatorFactory userStatisticsCalculatorFactory;

    private final AlbumStatisticsCalculatorFactory albumStatisticsCalculatorFactory;
    
    @KafkaListener(
            topics = USER_STATISTICS_TOPIC,
            containerFactory = "userStatisticsContainerFactory",
            batch = "true"
    )
    private void updateUsersStatistics(ConsumerRecords<String, UserStatisticsUpdateMessage> records) {
        Map<StatisticsType, Set<Long>> statisticsMap = new HashMap<>();
        records.forEach(record -> {
            Long userId = record.value().userId();
            StatisticsType statisticsType = StatisticsType.valueOf(record.key());
            statisticsMap.computeIfAbsent(statisticsType, type -> new HashSet<>()).add(userId);
            log.info("Received message for user update statistics userId={}, statisticsType={}", userId, statisticsType);
        });
        for (Map.Entry<StatisticsType, Set<Long>> entry : statisticsMap.entrySet()) {
            userStatisticsCalculatorFactory.findCalculator(entry.getKey())
                    .calculateStatistics(entry.getValue().stream().toList());
        }
    }
    
    @KafkaListener(
            topics = ALBUM_STATISTICS_TOPIC,
            containerFactory = "albumStatisticsContainerFactory"
    )
    private void updateAlbumsStatistics(ConsumerRecords<String, AlbumStatisticsUpdateMessage> records) {
        Map<StatisticsType, Set<String>> statisticsMap = new HashMap<>();
        records.forEach(record -> {
            String albumId = record.value().albumId();
            StatisticsType statisticsType = StatisticsType.valueOf(record.key());
            statisticsMap.computeIfAbsent(statisticsType, type -> new HashSet<>()).add(albumId);
            log.info("Received message for album update statistics albumId={}, statisticsType={}", albumId, statisticsType);
        });
        for (Map.Entry<StatisticsType, Set<String>> entry : statisticsMap.entrySet()) {
            albumStatisticsCalculatorFactory.findCalculator(entry.getKey())
                    .calculateStatistics(entry.getValue().stream().toList());
        }
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
                        .countOfWishlistItems(0L)
                        .countOfReviews(0L)
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
                        .countOfReviews(0L)
                        .averageGrade(0.0)
                        .build()
        );
        userStatisticsCache.put(userStatistics);

        return userStatistics;
    }
}
