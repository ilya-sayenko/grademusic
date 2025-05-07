package com.grademusic.main.model.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class UserStatisticsByGradesImpl implements UserStatisticsByGrades {

    private Long countOfGrades;

    private Double averageGrade;

    private LocalDate firstGradeDate;

    private LocalDate lastGradeDate;
}
