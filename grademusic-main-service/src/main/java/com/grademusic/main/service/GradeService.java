package com.grademusic.main.service;

import com.grademusic.main.controller.model.AlbumGradeSearchRequest;
import com.grademusic.main.controller.model.PaginatedRequest;
import com.grademusic.main.entity.AlbumGrade;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface GradeService {

    void gradeAlbum(long userId, String albumId, int grade);

    void updateAuditionDate(long userId, String albumId, LocalDate auditionDate);

    void deleteGrade(long userId, String albumId);

    AlbumGrade findGrade(long userId, String albumId);

    Page<AlbumGrade> findPaginatedGrades(AlbumGradeSearchRequest request, PaginatedRequest paginatedRequest);
}
