package com.grademusic.main.model.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TrackLastFm(
        String mbid,
        String name,
        int duration,
        String url,
        @JsonProperty("@attr")
        TrackAttributesLastFm attributes,
        ArtistLastFm artist
) {
}
