package com.grademusic.main.controller.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserStatisticsResponse(
        Double averageGrade,
        Long countOfGrades,
        LocalDate firstGradeDate,
        LocalDate lastGradeDate
) {
}
