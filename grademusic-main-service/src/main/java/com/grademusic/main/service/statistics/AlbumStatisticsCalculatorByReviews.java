package com.grademusic.main.service.statistics;

import com.grademusic.main.model.StatisticsType;
import com.grademusic.main.model.projection.AlbumStatisticsByReviews;
import com.grademusic.main.repository.AlbumReviewRepository;
import com.grademusic.main.repository.AlbumStatisticsRepository;
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

    @Override
    @Transactional
    public void calculateStatistics(List<String> albumIds) {
        List<AlbumStatisticsByReviews> albumStatistics = albumReviewRepository.calculateAlbumsStatistics(albumIds);
        for (AlbumStatisticsByReviews item : albumStatistics) {
            log.info("Saving album statistics by wishlist albumId={}, countOfReviews={}",
                    item.getAlbumId(), item.getCountOfReviews());
            albumStatisticsRepository.saveStatisticsByReviews(item);
        }
    }

    @Override
    public StatisticsType getType() {
        return StatisticsType.REVIEWS;
    }
}
