package com.grademusic.main.model.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AlbumMatchesLastFm(

        @JsonProperty("album")
        List<AlbumLastFm> albums
) {
}
