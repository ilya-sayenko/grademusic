package com.grademusic.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "grade-music.redis")
public class RedisConfig {
    private String url;
    private String password;
}
