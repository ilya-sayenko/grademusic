package com.grademusic.main.mapper;

import com.grademusic.main.config.MapperConfig;
import com.grademusic.main.controller.model.AlbumGradeResponse;
import com.grademusic.main.entity.AlbumGrade;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfig.class, uses = AlbumMapper.class)
public interface GradeMapper {

    AlbumGradeResponse toResponse(AlbumGrade albumGrade);

    List<AlbumGradeResponse> toResponse(List<AlbumGrade> userGrades);
}
