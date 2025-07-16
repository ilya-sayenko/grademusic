package com.grademusic.main.controller.model;

public record PaginatedRequest(
        Integer page,
        Integer perPage
) {
    public PaginatedRequest {
        if (page == null) {
            page = 0;
        }
        if (perPage == null) {
            perPage = 10;
        }
    }
}
