package com.grademusic.main.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class UserGrade {

    private Long userId;

    private Album album;

    private int userGrade;

    OffsetDateTime createDate;
}
