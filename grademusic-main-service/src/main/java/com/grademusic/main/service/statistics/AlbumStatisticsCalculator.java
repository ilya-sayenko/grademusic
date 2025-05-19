package com.grademusic.main.service.statistics;

import com.grademusic.main.model.StatisticsType;

import java.util.List;

public interface AlbumStatisticsCalculator {

    void calculateStatistics(List<String> albumIds);

    StatisticsType getType();
}
