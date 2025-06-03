package com.grademusic.main.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Data
@Configuration
public class LastFmConfig {

    @Value("${grade-music.last-fm.api-key}")
    private String apiKey;

    @Value("${grade-music.last-fm.base-url}")
    private String baseUrl;

    @Value("${grade-music.last-fm.connection.timeout.connect}")
    private Duration connectTimeout;

    @Value("${grade-music.last-fm.connection.timeout.read}")
    private Duration readTimeout;
}
