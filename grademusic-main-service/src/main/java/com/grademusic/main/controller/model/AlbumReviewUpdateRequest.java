package com.grademusic.main.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.With;

public record AlbumReviewUpdateRequest(
        Long id,

        @With
        Long userId,

        @NotNull
        Integer grade,

        @NotNull
        String text
) {
}
