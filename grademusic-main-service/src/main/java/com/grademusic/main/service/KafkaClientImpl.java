package com.grademusic.main.service;

import com.grademusic.main.model.message.AlbumStatisticsUpdateMessage;
import com.grademusic.main.model.StatisticsType;
import com.grademusic.main.model.message.UserStatisticsUpdateMessage;
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

    private final KafkaTemplate<String, AlbumStatisticsUpdateMessage> kafkaAlbumStatistics;

    private final KafkaTemplate<String, UserStatisticsUpdateMessage> kafkaUserStatistics;

    @Override
    public void sendUpdateAlbumStatistics(String albumId, StatisticsType type) {
        log.info("Sending message for updating album statistics albumId={}, statisticsType={}", albumId, type);
        kafkaAlbumStatistics.send(
                ALBUM_STATISTICS_TOPIC,
                type.name(),
                AlbumStatisticsUpdateMessage.builder()
                        .albumId(albumId)
                        .statisticsType(type)
                        .build()
        );
    }

    @Override
    public void sendUpdateUserStatistics(Long userId, StatisticsType type) {
        log.info("Sending message for updating user statistics userId={}, statisticsType={}", userId, type);
        kafkaUserStatistics.send(
                USER_STATISTICS_TOPIC,
                type.name(),
                UserStatisticsUpdateMessage.builder()
                        .userId(userId)
                        .statisticsType(type)
                        .build()
        );
    }
}
