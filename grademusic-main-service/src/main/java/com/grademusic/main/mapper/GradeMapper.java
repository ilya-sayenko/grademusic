package com.grademusic.main.mapper;

import com.grademusic.main.config.MapperConfig;
import com.grademusic.main.controller.model.AlbumGradePaginatedResponse;
import com.grademusic.main.controller.model.AlbumGradeResponse;
import com.grademusic.main.controller.model.PaginatedResponse;
import com.grademusic.main.entity.AlbumGrade;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface GradeMapper {

    AlbumGradeResponse toResponse(AlbumGrade albumGrade);

    List<AlbumGradeResponse> toResponse(List<AlbumGrade> userGrades);

    default AlbumGradePaginatedResponse toPaginatedResponse(Page<AlbumGrade> albumGrades) {
        Pageable pageable = albumGrades.getPageable();

        return AlbumGradePaginatedResponse.builder()
                .data(toResponse(albumGrades.getContent()))
                .pagination(
                        PaginatedResponse.builder()
                                .currentPage(pageable.getPageNumber())
                                .perPage(pageable.getPageSize())
                                .totalCount(albumGrades.getTotalElements())
                                .build()
                )
                .build();
    }
}
