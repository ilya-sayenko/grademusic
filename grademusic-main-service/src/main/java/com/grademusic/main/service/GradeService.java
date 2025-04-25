package com.grademusic.main.service;

import com.grademusic.main.controller.model.AlbumGradeSearchRequest;
import com.grademusic.main.entity.AlbumGrade;

import java.time.LocalDate;
import java.util.List;

public interface GradeService {

    void gradeAlbum(long userId, String albumId, int grade);

    void updateAuditionDate(long userId, String albumId, LocalDate auditionDate);

    void deleteGrade(long userId, String albumId);

    List<AlbumGrade> findGrades(AlbumGradeSearchRequest request);

    List<AlbumGrade> findGradesByUserIdAndAlbumIds(Long userId, List<String> albumIds);

    List<AlbumGrade> findGradesByUserId(Long userId);
}
