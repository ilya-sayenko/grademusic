package com.grademusic.main.controller.model;

import java.util.List;

public record AlbumSearchRequest(
        String albumName,
        List<String> albumIds
) {
}
