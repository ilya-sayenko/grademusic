package com.grademusic.main.service;

import com.grademusic.main.controller.model.AlbumReviewCreateRequest;
import com.grademusic.main.controller.model.AlbumReviewUpdateRequest;
import com.grademusic.main.controller.model.PaginatedRequest;
import com.grademusic.main.entity.AlbumReview;
import com.grademusic.main.exception.AlbumReviewNotFoundException;
import com.grademusic.main.mapper.ReviewMapper;
import com.grademusic.main.repository.AlbumReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.grademusic.main.model.StatisticsType.REVIEWS;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final AlbumReviewRepository albumReviewRepository;

    private final KafkaClient kafkaClient;

    private final ReviewMapper reviewMapper;

    @Override
    public void createAlbumReview(AlbumReviewCreateRequest albumReviewRequest) {
        AlbumReview albumReview = AlbumReview.builder()
                .userId(albumReviewRequest.userId())
                .albumId(albumReviewRequest.albumId())
                .grade(albumReviewRequest.grade())
                .text(albumReviewRequest.text())
                .build();
        albumReviewRepository.save(albumReview);
        kafkaClient.sendUpdateAlbumStatistics(albumReviewRequest.albumId(), REVIEWS);
        kafkaClient.sendUpdateUserStatistics(albumReviewRequest.userId(), REVIEWS);
    }

    @Override
    public void updateAlbumReview(AlbumReviewUpdateRequest albumReviewRequest) {
        AlbumReview albumReview = findReviewById(albumReviewRequest.id());
        if (!Objects.equals(albumReview.getUserId(), albumReviewRequest.userId())) {
            throw new AlbumReviewNotFoundException(String.format("Album review id=%d not found", albumReviewRequest.id()));
        }
        reviewMapper.updateReviewFromRequest(albumReviewRequest, albumReview);
        albumReviewRepository.save(albumReview);
    }

    @Override
    public Page<AlbumReview> findReviewsByAlbumId(String albumId, PaginatedRequest paginatedRequest) {
        PageRequest pageRequest = PageRequest.of(
                paginatedRequest.page(),
                paginatedRequest.perPage(),
                Sort.by(Sort.Direction.DESC, "createDate")
        );

        return albumReviewRepository.findByAlbumId(albumId, pageRequest);
    }

    @Override
    public AlbumReview findReviewById(Long reviewId) {
        return albumReviewRepository.findById(reviewId).orElseThrow(
                () -> new AlbumReviewNotFoundException(String.format("Album review id=%d not found", reviewId)));
    }
}
