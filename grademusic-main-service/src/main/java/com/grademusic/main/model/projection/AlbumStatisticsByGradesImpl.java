package com.grademusic.main.model.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AlbumStatisticsByGradesImpl implements AlbumStatisticsByGrades {

    private Long countOfGrades;

    private Double grade;
}
