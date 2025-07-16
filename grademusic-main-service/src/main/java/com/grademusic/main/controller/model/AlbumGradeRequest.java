package com.grademusic.main.controller.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AlbumGradeRequest(
        @NotNull
        String albumId,

        @Min(value = 1)
        @Max(value = 10)
        int grade
) {
}
