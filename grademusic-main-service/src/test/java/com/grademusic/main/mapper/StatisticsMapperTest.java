package com.grademusic.main.mapper;

import com.grademusic.main.controller.model.AlbumStatisticsResponse;
import com.grademusic.main.controller.model.UserStatisticsResponse;
import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.entity.UserStatistics;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StatisticsMapperTest {

    private final StatisticsMapper mapper = Mappers.getMapper(StatisticsMapper.class);

    @Test
    public void shouldMapToAlbumResponse() {
        AlbumStatistics albumStatistics = Instancio.create(AlbumStatistics.class);
        AlbumStatisticsResponse response = mapper.toResponse(albumStatistics);

        assertEquals(albumStatistics.getAlbumId(), response.albumId());
        assertEquals(albumStatistics.getGrade(), response.grade());
        assertEquals(albumStatistics.getCountOfGrades(), response.countOfGrades());
    }

    @Test
    public void shouldMapToUserResponse() {
        UserStatistics userStatistics = Instancio.create(UserStatistics.class);
        UserStatisticsResponse response = mapper.toResponse(userStatistics);

        assertEquals(userStatistics.getUserId(), response.userId());
        assertEquals(userStatistics.getAverageGrade(), response.averageGrade());
        assertEquals(userStatistics.getCountOfGrades(), response.countOfGrades());
        assertEquals(userStatistics.getFirstGradeDate(), response.firstGradeDate());
        assertEquals(userStatistics.getLastGradeDate(), response.lastGradeDate());
    }
}