package com.grademusic.main.service;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.entity.UserStatistics;

import java.util.List;

public interface StatisticsService {

    void updateAlbumStatistics(String albumId);

    void updateUserStatistics(Long userId);

    AlbumStatistics findAlbumStatisticsById(String albumId);

    List<AlbumStatistics> findAllAlbumStatisticsById(List<String> albumIds);

    UserStatistics findUserStatisticsById(Long userId);
}
