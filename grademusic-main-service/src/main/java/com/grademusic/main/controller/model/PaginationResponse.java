package com.grademusic.main.controller.model;

import lombok.Builder;

@Builder
public record PaginationResponse(
        Integer currentPage,
        Integer perPage,
        Long totalCount
) {
}
