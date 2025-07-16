package com.grademusic.main.service;

import com.grademusic.main.controller.model.AlbumReviewCreateRequest;
import com.grademusic.main.controller.model.AlbumReviewUpdateRequest;
import com.grademusic.main.controller.model.PaginatedRequest;
import com.grademusic.main.entity.AlbumReview;
import org.springframework.data.domain.Page;

public interface ReviewService {

    void createAlbumReview(AlbumReviewCreateRequest albumReviewRequest);

    void updateAlbumReview(AlbumReviewUpdateRequest albumReviewRequest);

    Page<AlbumReview> findReviewsByAlbumId(String albumId, PaginatedRequest paginatedRequest);

    AlbumReview findReviewById(Long reviewId);
}
