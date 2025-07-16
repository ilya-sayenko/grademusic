package com.grademusic.main.config;

import com.grademusic.main.model.message.AlbumStatisticsUpdateMessage;
import com.grademusic.main.model.message.UserStatisticsUpdateMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaConfig kafkaConfig;

    @Bean
    public ProducerFactory<String, AlbumStatisticsUpdateMessage> producerAlbumStatisticsFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, "grademusic-service-producer-album-statistics");

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ProducerFactory<String, UserStatisticsUpdateMessage> producerUserStatisticsFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, "grademusic-service-producer-user-statistics");

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, AlbumStatisticsUpdateMessage> kafkaAlbumStatisticsTemplate() {
        return new KafkaTemplate<>(producerAlbumStatisticsFactory());
    }

    @Bean
    public KafkaTemplate<String, UserStatisticsUpdateMessage> kafkaUserStatisticsTemplate() {
        return new KafkaTemplate<>(producerUserStatisticsFactory());
    }
}
