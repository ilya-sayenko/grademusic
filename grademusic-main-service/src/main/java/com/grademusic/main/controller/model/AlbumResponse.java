package com.grademusic.main.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AlbumResponse(
        String id,
        String name,
        String artist,
        ImageResponse image,
        List<TrackResponse> tracks
) {
}
