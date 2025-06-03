package com.grademusic.main.service;

import com.grademusic.main.model.StatisticsType;

public interface KafkaClient {

    void sendUpdateAlbumStatistics(String albumId, StatisticsType statisticsType);

    void sendUpdateUserStatistics(Long userId, StatisticsType statisticsType);
}
