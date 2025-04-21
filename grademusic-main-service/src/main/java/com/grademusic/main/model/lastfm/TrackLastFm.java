package com.grademusic.main.model.lastfm;

public record TrackLastFm(
        String name,
        int duration,
        String url,
        String streamable,
        String mbid
) {
}
