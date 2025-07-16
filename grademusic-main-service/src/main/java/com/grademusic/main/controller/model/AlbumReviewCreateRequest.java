package com.grademusic.main.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.With;

public record AlbumReviewCreateRequest(
        @With
        Long userId,

        @NotNull
        String albumId,

        @NotNull
        Integer grade,

        @NotNull
        String text
) {
}
