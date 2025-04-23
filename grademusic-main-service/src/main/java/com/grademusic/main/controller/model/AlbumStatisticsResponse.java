package com.grademusic.main.controller.model;

import lombok.Builder;

@Builder
public record AlbumStatisticsResponse(
        String albumId,
        Double grade,
        Long countOfGrades
) {
}
