package com.grademusic.main.service.statistics;

import com.grademusic.main.model.StatisticsType;

import java.util.List;

public interface UserStatisticsCalculator {

    void calculateStatistics(List<Long> userIds);

    StatisticsType getType();
}
