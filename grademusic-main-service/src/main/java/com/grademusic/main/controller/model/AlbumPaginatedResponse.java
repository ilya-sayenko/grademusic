package com.grademusic.main.controller.model;

import lombok.Builder;

import java.util.List;

@Builder
public record AlbumPaginatedResponse(
        List<AlbumResponse> data,
        PaginatedResponse pagination
) {
}
