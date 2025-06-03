package com.grademusic.auth.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private final RedisConfig redisConfig;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisConfig.getUrl())
                .setPassword(redisConfig.getPassword())
                .setConnectionPoolSize(1)
                .setConnectionMinimumIdleSize(1);

        return Redisson.create(config);
    }
}
