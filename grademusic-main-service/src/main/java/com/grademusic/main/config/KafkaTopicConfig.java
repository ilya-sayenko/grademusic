package com.grademusic.main.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

import static com.grademusic.main.config.KafkaConfig.ALBUM_STATISTICS_TOPIC;
import static com.grademusic.main.config.KafkaConfig.USER_STATISTICS_TOPIC;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaConfig kafkaConfig;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());

        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicAlbumStatisticsUpdate() {
        return TopicBuilder.name(ALBUM_STATISTICS_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicUserStatisticsUpdate() {
        return TopicBuilder.name(USER_STATISTICS_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
