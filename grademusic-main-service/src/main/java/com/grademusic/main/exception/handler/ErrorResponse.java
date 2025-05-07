package com.grademusic.main.exception.handler;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String error
){
}
