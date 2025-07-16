package com.grademusic.main.controller.model;

import java.time.OffsetDateTime;

public record AlbumReviewResponse(
        Long userId,
        String albumId,
        Integer grade,
        String text,
        OffsetDateTime createDate
) {
}
