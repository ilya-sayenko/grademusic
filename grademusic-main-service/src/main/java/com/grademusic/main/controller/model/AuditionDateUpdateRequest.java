package com.grademusic.main.controller.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AuditionDateUpdateRequest(
        @NotNull
        String albumId,

        @NotNull
        LocalDate auditionDate
) {
}
