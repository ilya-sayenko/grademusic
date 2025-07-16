package com.grademusic.main.config;

import com.grademusic.main.model.message.AlbumStatisticsUpdateMessage;
import com.grademusic.main.model.message.UserStatisticsUpdateMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static com.grademusic.main.config.KafkaConfig.GENERAL_CONSUMER_GROUP_ID;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaConfig kafkaConfig;

    @Bean
    public ConsumerFactory<String, AlbumStatisticsUpdateMessage> albumStatisticsConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GENERAL_CONSUMER_GROUP_ID);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "20");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(AlbumStatisticsUpdateMessage.class)
        );
    }

    @Bean
    public ConsumerFactory<String, UserStatisticsUpdateMessage> userStatisticsConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GENERAL_CONSUMER_GROUP_ID);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "20");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(UserStatisticsUpdateMessage.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AlbumStatisticsUpdateMessage> albumStatisticsContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AlbumStatisticsUpdateMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(albumStatisticsConsumerFactory());
        factory.setBatchListener(true);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserStatisticsUpdateMessage> userStatisticsContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserStatisticsUpdateMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userStatisticsConsumerFactory());
        factory.setBatchListener(true);

        return factory;
    }
}
