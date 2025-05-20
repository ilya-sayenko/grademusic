package com.grademusic.main.service.statistics;

import com.grademusic.main.model.StatisticsType;
import com.grademusic.main.model.projection.UserStatisticsByGrades;
import com.grademusic.main.repository.AlbumGradeRepository;
import com.grademusic.main.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserStatisticsCalculatorByGrades implements UserStatisticsCalculator {

    private final UserStatisticsRepository userStatisticsRepository;

    private final AlbumGradeRepository albumGradeRepository;

    @Override
    @Transactional
    public void calculateStatistics(List<Long> userIds) {
        List<UserStatisticsByGrades> userStatistics = albumGradeRepository.calculateUsersStatistics(userIds);
        for (UserStatisticsByGrades item : userStatistics) {
            log.info("Saving user statistics by wishlist userId={}, averageGrade={}, countOfGrades={}, firstGradeDate={}, lastGradeDate={}",
                    item.getUserId(), item.getAverageGrade(), item.getCountOfGrades(), item.getFirstGradeDate(), item.getLastGradeDate());
            userStatisticsRepository.saveStatisticsByGrades(item);
        }
    }

    @Override
    public StatisticsType getType() {
        return StatisticsType.GRADES;
    }
}
