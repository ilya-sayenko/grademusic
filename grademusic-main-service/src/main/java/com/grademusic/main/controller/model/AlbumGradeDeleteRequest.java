package com.grademusic.main.controller.model;

import jakarta.validation.constraints.NotNull;

public record AlbumGradeDeleteRequest(
        @NotNull
        String albumId
) {
}
