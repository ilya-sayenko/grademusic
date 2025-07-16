package com.grademusic.main.service.http;

import com.grademusic.main.model.lastfm.AlbumInfoLastFm;
import com.grademusic.main.model.lastfm.AlbumSearchRootLastFm;

public interface LastFmClient {

    AlbumSearchRootLastFm albumSearch(String album);

    AlbumInfoLastFm albumGetInfo(String mbid);
}
