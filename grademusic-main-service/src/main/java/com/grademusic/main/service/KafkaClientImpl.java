package com.grademusic.main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.grademusic.main.config.KafkaConfig.ALBUM_STATISTICS_TOPIC;
import static com.grademusic.main.config.KafkaConfig.USER_STATISTICS_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaClientImpl implements KafkaClient {

    private final KafkaTemplate<String, String> kafkaAlbumStatistics;

    private final KafkaTemplate<String, Long> kafkaUserStatistics;

    @Override
    public void sendUpdateAlbumStatistics(String albumId) {
        kafkaAlbumStatistics.send(ALBUM_STATISTICS_TOPIC, albumId);
    }

    @Override
    public void sendUpdateUserStatistics(Long userId) {
        kafkaUserStatistics.send(USER_STATISTICS_TOPIC, userId);
    }
}
