package com.grademusic.main.model.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ImageLastFm(
        @JsonProperty("#text")
        String text,
        String size
) {
}
