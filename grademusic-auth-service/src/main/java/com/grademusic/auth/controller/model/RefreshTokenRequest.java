package com.grademusic.auth.controller.model;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull
        String refreshToken
) {
}
