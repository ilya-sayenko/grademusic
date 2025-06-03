package com.grademusic.main.controller.model;

import lombok.Builder;

import java.util.List;

@Builder
public record AlbumGradePaginatedResponse(
        List<AlbumGradeResponse> data,
        PaginatedResponse pagination
) {
}
