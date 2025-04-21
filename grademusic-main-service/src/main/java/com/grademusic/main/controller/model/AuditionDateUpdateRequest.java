package com.grademusic.main.controller.model;

import java.time.LocalDate;

public record AuditionDateUpdateRequest(
        String albumId,
        LocalDate auditionDate
) {
}
