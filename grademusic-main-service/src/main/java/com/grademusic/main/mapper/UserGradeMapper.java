package com.grademusic.main.mapper;

import com.grademusic.main.config.MapperConfig;
import com.grademusic.main.controller.model.UserGradeResponse;
import com.grademusic.main.model.UserGrade;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfig.class, uses = AlbumMapper.class)
public interface UserGradeMapper {

    UserGradeResponse toResponse(UserGrade userGrade);

    List<UserGradeResponse> toResponse(List<UserGrade> userGrades);
}
