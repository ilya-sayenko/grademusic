package com.grademusic.main.mapper;

import com.grademusic.main.controller.model.AlbumGradeResponse;
import com.grademusic.main.entity.AlbumGrade;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GradeMapperTest {

    public final GradeMapper mapper = Mappers.getMapper(GradeMapper.class);

    @Test
    public void shouldMapToResponse() {
        AlbumGrade albumGrade = Instancio.create(AlbumGrade.class);
        AlbumGradeResponse response = mapper.toResponse(albumGrade);

        assertEquals(albumGrade.getUserId(), response.userId());
        assertEquals(albumGrade.getAlbumId(), response.albumId());
        assertEquals(albumGrade.getGrade(), response.grade());
        assertEquals(albumGrade.getCreateDate(), response.createDate());
        assertEquals(albumGrade.getAuditionDate(), response.auditionDate());
    }
}
