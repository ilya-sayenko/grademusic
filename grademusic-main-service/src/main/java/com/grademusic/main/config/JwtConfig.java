package com.grademusic.main.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JwtConfig {

    @Value("${grade-music.jwt.secret-key}")
    private String secretKey;
}
