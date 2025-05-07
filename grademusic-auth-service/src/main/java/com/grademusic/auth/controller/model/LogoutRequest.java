package com.grademusic.auth.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.With;

public record LogoutRequest(
        @With
        @NotNull
        String accessToken,

        @NotNull
        String refreshToken
) {
}
