package com.grademusic.main.model.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AlbumSearchResultsLastFm(

        @JsonProperty("opensearch:totalResults")
        Long totalResults,

        @JsonProperty("opensearch:startIndex")
        Long startIndex,

        @JsonProperty("opensearch:itemsPerPage")
        Long itemsPerPage,

        @JsonProperty("albummatches")
        AlbumMatchesLastFm albumMatches
) {
}
