package com.grademusic.main.service;

public interface KafkaClient {

    void sendUpdateAlbumStatistics(String albumId);

    void sendUpdateUserStatistics(Long userId);
}
