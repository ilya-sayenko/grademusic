package com.grademusic.main.service.statistics;

import com.grademusic.main.model.StatisticsType;
import com.grademusic.main.model.projection.AlbumStatisticsByWishlist;
import com.grademusic.main.repository.AlbumStatisticsRepository;
import com.grademusic.main.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlbumStatisticsCalculatorByWishlist implements AlbumStatisticsCalculator {

    private final WishlistRepository wishlistRepository;

    private final AlbumStatisticsRepository albumStatisticsRepository;

    @Override
    @Transactional
    public void calculateStatistics(List<String> albumIds) {
        List<AlbumStatisticsByWishlist> albumStatistics = wishlistRepository.calculateAlbumsStatistics(albumIds);
        for (AlbumStatisticsByWishlist item : albumStatistics) {
            albumStatisticsRepository.saveStatisticsByWishlist(item);
        }
    }

    @Override
    public StatisticsType getType() {
        return StatisticsType.WISHLIST;
    }
}
