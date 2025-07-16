package com.grademusic.main.controller.model;

import lombok.Builder;

@Builder
public record ImageResponse(
        String small,
        String medium,
        String large,
        String extralarge
) {
}
