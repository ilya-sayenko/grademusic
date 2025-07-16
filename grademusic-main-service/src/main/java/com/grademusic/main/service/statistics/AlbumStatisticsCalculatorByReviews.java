package com.grademusic.main.service.statistics;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.model.StatisticsType;
import com.grademusic.main.model.projection.AlbumStatisticsByReviews;
import com.grademusic.main.repository.AlbumReviewRepository;
import com.grademusic.main.repository.AlbumStatisticsRepository;
import com.grademusic.main.service.cache.AlbumStatisticsCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlbumStatisticsCalculatorByReviews implements AlbumStatisticsCalculator {

    private final AlbumReviewRepository albumReviewRepository;

    private final AlbumStatisticsRepository albumStatisticsRepository;

    private final AlbumStatisticsCache albumStatisticsCache;

    @Override
    @Transactional
    public void calculateStatistics(List<String> albumIds) {
        List<AlbumStatisticsByReviews> albumStatistics = albumReviewRepository.calculateAlbumsStatistics(albumIds);
        for (AlbumStatisticsByReviews item : albumStatistics) {
            log.info("Saving album statistics by reviews albumId={}, countOfReviews={}",
                    item.getAlbumId(), item.getCountOfReviews());
            AlbumStatistics savedAlbumStatistics = albumStatisticsRepository.saveStatisticsByReviews(item);
            albumStatisticsCache.put(savedAlbumStatistics);
        }
    }

    @Override
    public StatisticsType getType() {
        return StatisticsType.REVIEWS;
    }
}
