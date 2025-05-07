package com.grademusic.main.controller.model;

import lombok.Builder;

@Builder
public record PaginatedResponse(
        Integer currentPage,
        Integer perPage,
        Long totalCount
) {
}
