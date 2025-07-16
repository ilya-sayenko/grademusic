package com.grademusic.main.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TrackResponse(
        String id,
        String name,
        int duration,
        int order
) {
}
