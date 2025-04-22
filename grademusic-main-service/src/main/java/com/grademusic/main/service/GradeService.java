package com.grademusic.main.service;

import java.time.LocalDate;

public interface GradeService {

    void gradeAlbum(long userId, String albumId, int grade);

    void updateAuditionDate(long userId, String albumId, LocalDate auditionDate);

    void deleteGrade(long userId, String albumId);
}
