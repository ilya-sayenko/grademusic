package com.grademusic.main.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Data
@Configuration
@ConfigurationProperties(prefix = "grade-music.redis")
public class RedisConfig {

    private String url;

    private String password;

    private Duration cacheExpiration;
}
