package com.grademusic.main.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "grade-music.kafka")
public class KafkaConfig {

    public static final String ALBUM_STATISTICS_TOPIC = "statistics_update_album";

    public static final String USER_STATISTICS_TOPIC = "statistics_update_user";

    public static final String GENERAL_CONSUMER_GROUP_ID = "grademusic_general_group";

    private String bootstrapServers;
}
