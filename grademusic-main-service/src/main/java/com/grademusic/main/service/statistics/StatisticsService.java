package com.grademusic.main.service.statistics;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.entity.UserStatistics;

import java.util.List;

public interface StatisticsService {

    AlbumStatistics findAlbumStatisticsById(String albumId);

    List<AlbumStatistics> findAllAlbumStatisticsById(List<String> albumIds);

    UserStatistics findUserStatisticsById(Long userId);
}
