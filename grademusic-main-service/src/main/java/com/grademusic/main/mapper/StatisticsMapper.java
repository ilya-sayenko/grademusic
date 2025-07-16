package com.grademusic.main.mapper;

import com.grademusic.main.config.MapperConfig;
import com.grademusic.main.controller.model.AlbumStatisticsResponse;
import com.grademusic.main.controller.model.UserStatisticsResponse;
import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.entity.UserStatistics;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface StatisticsMapper {

    AlbumStatisticsResponse toResponse(AlbumStatistics albumStatistics);

    UserStatisticsResponse toResponse(UserStatistics userStatistics);
}
