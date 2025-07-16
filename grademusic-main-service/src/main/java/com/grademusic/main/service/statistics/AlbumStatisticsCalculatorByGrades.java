package com.grademusic.main.service.statistics;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.model.StatisticsType;
import com.grademusic.main.model.projection.AlbumStatisticsByGrades;
import com.grademusic.main.repository.AlbumGradeRepository;
import com.grademusic.main.repository.AlbumStatisticsRepository;
import com.grademusic.main.service.cache.AlbumCache;
import com.grademusic.main.service.cache.AlbumStatisticsCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlbumStatisticsCalculatorByGrades implements AlbumStatisticsCalculator {

    private final AlbumGradeRepository albumGradeRepository;

    private final AlbumStatisticsRepository albumStatisticsRepository;

    private final AlbumCache albumCache;

    private final AlbumStatisticsCache albumStatisticsCache;

    @Override
    @Transactional
    public void calculateStatistics(List<String> albumIds) {
        List<AlbumStatisticsByGrades> albumStatistics = albumGradeRepository.calculateAlbumsStatistics(albumIds);
        for (AlbumStatisticsByGrades item : albumStatistics) {
            log.info("Saving album statistics by grades albumId={}, countOfGrades={}, grade={}",
                    item.getAlbumId(), item.getCountOfGrades(), item.getGrade());
            AlbumStatistics savedAlbumStatistics = albumStatisticsRepository.saveStatisticsByGrades(item);
            albumCache.findById(item.getAlbumId()).ifPresent(cachedAlbum -> {
                cachedAlbum.setGrade(item.getGrade());
                albumCache.put(cachedAlbum);
            });
            albumStatisticsCache.put(savedAlbumStatistics);
        }
    }

    @Override
    public StatisticsType getType() {
        return StatisticsType.GRADES;
    }
}
