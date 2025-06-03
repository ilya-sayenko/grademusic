package com.grademusic.main.controller.model;

import lombok.Builder;

import java.util.List;

@Builder
public record AlbumReviewPaginatedResponse(
        List<AlbumReviewResponse> data,
        PaginatedResponse pagination
) {
}
