package com.grademusic.main.model;

import lombok.Builder;

import java.util.List;

@Builder
public record User(
        Long id,
        String username,
        List<String> roles
){
}
