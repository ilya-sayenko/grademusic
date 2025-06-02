package com.grademusic.main.service.statistics;

import com.grademusic.main.entity.UserStatistics;
import com.grademusic.main.model.StatisticsType;
import com.grademusic.main.model.projection.UserStatisticsByWishlist;
import com.grademusic.main.repository.UserStatisticsRepository;
import com.grademusic.main.repository.WishlistRepository;
import com.grademusic.main.service.cache.UserStatisticsCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserStatisticsCalculatorByWishlist implements UserStatisticsCalculator {

    private final UserStatisticsRepository userStatisticsRepository;

    private final WishlistRepository wishlistRepository;

    private final UserStatisticsCache userStatisticsCache;

    @Override
    @Transactional
    public void calculateStatistics(List<Long> userIds) {
        List<UserStatisticsByWishlist> userStatistics = wishlistRepository.calculateUsersStatistics(userIds);
        for (UserStatisticsByWishlist item : userStatistics) {
            log.info("Saving user statistics by wishlist userId={}, countOfWishlistItems={}",
                    item.getUserId(), item.getCountOfWishlistItems());
            UserStatistics savedUserStatistics = userStatisticsRepository.saveStatisticsByWishlist(item);
            userStatisticsCache.put(savedUserStatistics);
        }
    }

    @Override
    public StatisticsType getType() {
        return StatisticsType.WISHLIST;
    }
}
