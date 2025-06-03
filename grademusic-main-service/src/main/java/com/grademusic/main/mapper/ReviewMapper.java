package com.grademusic.main.mapper;

import com.grademusic.main.config.MapperConfig;
import com.grademusic.main.controller.model.AlbumReviewPaginatedResponse;
import com.grademusic.main.controller.model.AlbumReviewResponse;
import com.grademusic.main.controller.model.AlbumReviewUpdateRequest;
import com.grademusic.main.controller.model.PaginatedResponse;
import com.grademusic.main.entity.AlbumReview;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {

    AlbumReviewResponse toResponse(AlbumReview albumReview);

    List<AlbumReviewResponse> toResponse(List<AlbumReview> albumReviews);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReviewFromRequest(AlbumReviewUpdateRequest projectRequest, @MappingTarget AlbumReview albumReview);

    default AlbumReviewPaginatedResponse toPaginatedResponse(Page<AlbumReview> albumReviews) {
        Pageable pageable = albumReviews.getPageable();

        return AlbumReviewPaginatedResponse.builder()
                .data(toResponse(albumReviews.getContent()))
                .pagination(
                        PaginatedResponse.builder()
                                .currentPage(pageable.getPageNumber())
                                .perPage(pageable.getPageSize())
                                .totalCount(albumReviews.getTotalElements())
                                .build()
                )
                .build();
    }
}
