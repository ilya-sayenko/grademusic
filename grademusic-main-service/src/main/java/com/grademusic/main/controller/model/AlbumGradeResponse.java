package com.grademusic.main.controller.model;

import lombok.Builder;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Builder
public record AlbumGradeResponse(
        long userId,
        String albumId,
        int grade,
        LocalDate auditionDate,
        OffsetDateTime createDate
) {
}
