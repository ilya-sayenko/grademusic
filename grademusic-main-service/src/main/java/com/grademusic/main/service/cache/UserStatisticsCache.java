package com.grademusic.main.service.cache;

import com.grademusic.main.entity.UserStatistics;

import java.util.Optional;

public interface UserStatisticsCache {

    Optional<UserStatistics> findById(Long userId);

    void put(UserStatistics userStatistics);
}
