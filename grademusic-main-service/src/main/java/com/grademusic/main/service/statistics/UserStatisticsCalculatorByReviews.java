package com.grademusic.main.service.statistics;

import com.grademusic.main.entity.UserStatistics;
import com.grademusic.main.model.StatisticsType;
import com.grademusic.main.model.projection.UserStatisticsByReviews;
import com.grademusic.main.repository.AlbumReviewRepository;
import com.grademusic.main.repository.UserStatisticsRepository;
import com.grademusic.main.service.cache.UserStatisticsCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserStatisticsCalculatorByReviews implements UserStatisticsCalculator {

    private final UserStatisticsRepository userStatisticsRepository;

    private final AlbumReviewRepository albumReviewRepository;

    private final UserStatisticsCache userStatisticsCache;

    @Override
    @Transactional
    public void calculateStatistics(List<Long> userIds) {
        List<UserStatisticsByReviews> userStatistics = albumReviewRepository.calculateUsersStatistics(userIds);
        for (UserStatisticsByReviews item : userStatistics) {
            log.info("Saving user statistics by reviews userId={}, countOfReviews={}",
                    item.getUserId(), item.getCountOfReviews());
            UserStatistics savedUserStatistics = userStatisticsRepository.saveStatisticsByReviews(item);
            userStatisticsCache.put(savedUserStatistics);
        }
    }

    @Override
    public StatisticsType getType() {
        return StatisticsType.REVIEWS;
    }
}
