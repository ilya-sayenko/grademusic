package com.grademusic.main.controller;

import com.grademusic.main.controller.model.AlbumReviewCreateRequest;
import com.grademusic.main.controller.model.AlbumReviewPaginatedResponse;
import com.grademusic.main.controller.model.AlbumReviewUpdateRequest;
import com.grademusic.main.controller.model.AlbumReviewResponse;
import com.grademusic.main.controller.model.PaginatedRequest;
import com.grademusic.main.mapper.ReviewMapper;
import com.grademusic.main.service.ReviewService;
import com.grademusic.main.utils.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/grade-music/main/albums")
@RequiredArgsConstructor
public class AlbumReviewController {

    private final ReviewService reviewService;

    private final ReviewMapper reviewMapper;

    @GetMapping("/{albumId}/reviews")
    public AlbumReviewPaginatedResponse findReviewsByAlbumId(
            @PathVariable("albumId") String albumId,
            PaginatedRequest paginatedRequest
    ) {
        return reviewMapper.toPaginatedResponse(reviewService.findReviewsByAlbumId(albumId, paginatedRequest));
    }

    @GetMapping("/reviews/{reviewId}")
    public AlbumReviewResponse findReviewsById(@PathVariable("reviewId") Long reviewId) {
        return reviewMapper.toResponse(reviewService.findReviewById(reviewId));
    }

    @PostMapping("/reviews")
    public void createReview(@RequestBody @Valid AlbumReviewCreateRequest request, Authentication authentication) {
        reviewService.createAlbumReview(request.withUserId(AuthUtils.extractUser(authentication).id()));
    }

    @PutMapping("/reviews")
    public void updateReview(@RequestBody @Valid AlbumReviewUpdateRequest request, Authentication authentication) {
        reviewService.updateAlbumReview(request.withUserId(AuthUtils.extractUser(authentication).id()));
    }
}
