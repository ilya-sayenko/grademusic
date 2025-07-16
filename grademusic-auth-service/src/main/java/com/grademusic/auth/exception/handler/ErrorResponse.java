package com.grademusic.auth.exception.handler;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String error
){
}
