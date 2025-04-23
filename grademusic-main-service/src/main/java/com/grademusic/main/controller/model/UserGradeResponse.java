package com.grademusic.main.controller.model;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record UserGradeResponse(
        int userGrade,
        OffsetDateTime createDate,
        AlbumResponse album
) {
}
