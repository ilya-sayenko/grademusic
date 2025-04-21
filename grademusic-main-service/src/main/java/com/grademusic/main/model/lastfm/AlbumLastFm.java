package com.grademusic.main.model.lastfm;

import java.util.ArrayList;

public record AlbumLastFm(
        String name,
        String artist,
        String url,
        ArrayList<ImageLastFm> image,
        String streamable,
        String mbid
) {
}
