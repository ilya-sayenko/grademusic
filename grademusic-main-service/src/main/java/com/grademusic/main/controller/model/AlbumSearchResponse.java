package com.grademusic.main.controller.model;

public record AlbumSearchResponse(
        String id,
        String name,
        String artist,
        ImageResponse image
) {
}
