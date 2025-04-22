package com.grademusic.main.model.projection;

import java.time.LocalDate;

public interface UserStatisticsByGrades {

    Long getCountOfGrades();

    Double getAverageGrade();

    LocalDate getFirstGradeDate();

    LocalDate getLastGradeDate();
}
