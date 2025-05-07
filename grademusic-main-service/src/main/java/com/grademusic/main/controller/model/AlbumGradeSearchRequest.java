package com.grademusic.main.controller.model;

import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder
public record AlbumGradeSearchRequest(
        @With
        Long userId,
        List<String> albumIds
) {
}
