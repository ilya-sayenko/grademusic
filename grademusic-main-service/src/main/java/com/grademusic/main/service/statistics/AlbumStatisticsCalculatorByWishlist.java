package com.grademusic.main.service.statistics;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.model.StatisticsType;
import com.grademusic.main.model.projection.AlbumStatisticsByWishlist;
import com.grademusic.main.repository.AlbumStatisticsRepository;
import com.grademusic.main.repository.WishlistRepository;
import com.grademusic.main.service.cache.AlbumStatisticsCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlbumStatisticsCalculatorByWishlist implements AlbumStatisticsCalculator {

    private final WishlistRepository wishlistRepository;

    private final AlbumStatisticsRepository albumStatisticsRepository;

    private final AlbumStatisticsCache albumStatisticsCache;

    @Override
    @Transactional
    public void calculateStatistics(List<String> albumIds) {
        List<AlbumStatisticsByWishlist> albumStatistics = wishlistRepository.calculateAlbumsStatistics(albumIds);
        for (AlbumStatisticsByWishlist item : albumStatistics) {
            log.info("Saving album statistics by wishlist albumId={}, countOfWishlistItems={}",
                    item.getAlbumId(), item.getCountOfWishlistItems());
            AlbumStatistics savedAlbumStatistics = albumStatisticsRepository.saveStatisticsByWishlist(item);
            albumStatisticsCache.put(savedAlbumStatistics);
        }
    }

    @Override
    public StatisticsType getType() {
        return StatisticsType.WISHLIST;
    }
}
