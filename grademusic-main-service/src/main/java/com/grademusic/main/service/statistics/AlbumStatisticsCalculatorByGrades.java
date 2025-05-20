package com.grademusic.main.service.statistics;

import com.grademusic.main.model.Album;
import com.grademusic.main.model.StatisticsType;
import com.grademusic.main.model.projection.AlbumStatisticsByGrades;
import com.grademusic.main.repository.AlbumGradeRepository;
import com.grademusic.main.repository.AlbumStatisticsRepository;
import com.grademusic.main.service.cache.AlbumCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlbumStatisticsCalculatorByGrades implements AlbumStatisticsCalculator {

    private final AlbumGradeRepository albumGradeRepository;

    private final AlbumStatisticsRepository albumStatisticsRepository;

    private final AlbumCache albumCache;

    @Override
    @Transactional
    public void calculateStatistics(List<String> albumIds) {
        List<AlbumStatisticsByGrades> albumStatistics = albumGradeRepository.calculateAlbumsStatistics(albumIds);
        for (AlbumStatisticsByGrades item : albumStatistics) {
            albumStatisticsRepository.saveStatisticsByGrades(item);
            log.info("Saving album statistics by grades albumId={}, countOfGrades={}, grade={}",
                    item.getAlbumId(), item.getCountOfGrades(), item.getGrade());
            Optional<Album> cachedAlbumOpt = albumCache.findById(item.getAlbumId());
            if (cachedAlbumOpt.isPresent()) {
                Album cachedAlbum = cachedAlbumOpt.get();
                cachedAlbum.setGrade(item.getGrade());
                albumCache.put(cachedAlbum);
            }
        }
    }

    @Override
    public StatisticsType getType() {
        return StatisticsType.GRADES;
    }
}
