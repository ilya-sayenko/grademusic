package com.grademusic.main.controller.model;

import jakarta.validation.constraints.NotNull;

public record AlbumReviewResponse(
        @NotNull
        Long userId,

        @NotNull
        String albumId,

        @NotNull
        Integer grade,

        @NotNull
        String text
) {
}
