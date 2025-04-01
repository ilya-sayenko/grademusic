package com.grademusic.main.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class LastFmConfig {

    @Value("${grade-music.last-fm.api-key}")
    private String secretKey;
}
