package com.grademusic.main.model.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TracksRootLastFm(
        @JsonProperty("track")
        List<TrackLastFm> tracks
) {
}
