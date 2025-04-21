package com.grademusic.main.model.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageLastFm(

        @JsonProperty("#text")
        String text,

        String size
) {
}
