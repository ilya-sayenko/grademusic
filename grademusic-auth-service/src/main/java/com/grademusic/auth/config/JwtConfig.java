package com.grademusic.auth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Data
@Configuration
public class JwtConfig {

    @Value("${grade-music.jwt.secret-key}")
    private String secretKey;

    @Value("${grade-music.jwt.expiration}")
    private Duration accessExpiration;

    @Value("${grade-music.jwt.refresh.expiration}")
    private Duration refreshExpiration;
}
