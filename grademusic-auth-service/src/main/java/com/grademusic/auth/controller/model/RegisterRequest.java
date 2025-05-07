package com.grademusic.auth.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotNull
        String username,

        @NotNull
        @Email
        String email,

        @NotNull
        String password
) {
}
