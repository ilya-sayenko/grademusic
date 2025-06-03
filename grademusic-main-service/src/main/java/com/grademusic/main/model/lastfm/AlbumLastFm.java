package com.grademusic.main.model.lastfm;

import java.util.List;

public record AlbumLastFm(
        String mbid,
        String name,
        String artist,
        String url,
        List<ImageLastFm> image,
        TracksRootLastFm tracks
) {
}
