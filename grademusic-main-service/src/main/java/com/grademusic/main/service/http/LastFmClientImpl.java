package com.grademusic.main.service.http;

import com.grademusic.main.config.LastFmConfig;
import com.grademusic.main.model.lastfm.AlbumInfoLastFm;
import com.grademusic.main.model.lastfm.AlbumSearchRootLastFm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class LastFmClientImpl implements LastFmClient {

    private final RestClient lastFmRestClient;

    private final LastFmConfig lastFmConfig;

    @Override
    public AlbumSearchRootLastFm albumSearch(String album) {
        return lastFmRestClient.get()
                .uri(UriComponentsBuilder.fromUriString(lastFmConfig.getBaseUrl())
                        .queryParam("method", "album.search")
                        .queryParam("album", album)
                        .queryParam("api_key", lastFmConfig.getApiKey())
                        .queryParam("format", "json")
                        .build()
                        .toUri())
                .retrieve()
                .body(AlbumSearchRootLastFm.class);
    }

    @Override
    public AlbumInfoLastFm albumGetInfo(String mbid) {
        return lastFmRestClient.get()
                .uri(UriComponentsBuilder.fromUriString(lastFmConfig.getBaseUrl())
                        .queryParam("method", "album.getinfo")
                        .queryParam("mbid", mbid)
                        .queryParam("api_key", lastFmConfig.getApiKey())
                        .queryParam("format", "json")
                        .build()
                        .toUri())
                .retrieve()
                .body(AlbumInfoLastFm.class);
    }
}
