package com.grademusic.main.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class LastFmRestClientConfig {

    private final LastFmConfig lastFmConfig;

    @Bean
    public RestTemplate lastFmRestTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(lastFmConfig.getConnectTimeout())
                .setReadTimeout(lastFmConfig.getReadTimeout())
                .build();
    }

    @Bean
    public RestClient lastFmRestClient(RestTemplate lastFmRestTemplate) {
        return RestClient.builder(lastFmRestTemplate)
                .build();
    }
}
