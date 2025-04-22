package com.grademusic.main.service;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.entity.UserStatistics;

public interface StatisticsService {

    void updateAlbumStatistics(String albumId);

    void updateUserStatistics(Long userId);

    AlbumStatistics findAlbumStatisticsById(String albumId);

    UserStatistics findUserStatisticsById(Long userId);
}
