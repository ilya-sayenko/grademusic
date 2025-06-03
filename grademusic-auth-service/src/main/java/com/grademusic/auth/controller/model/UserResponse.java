package com.grademusic.auth.controller.model;

import java.time.OffsetDateTime;

public record UserResponse(
        long id,
        String username,
        String email,
        OffsetDateTime createDate
) {
}
