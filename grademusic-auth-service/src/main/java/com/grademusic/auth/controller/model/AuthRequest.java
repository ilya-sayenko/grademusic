package com.grademusic.auth.controller.model;

import jakarta.validation.constraints.NotNull;

public record AuthRequest(
        @NotNull
        String username,
        @NotNull
        String password
) {
}
