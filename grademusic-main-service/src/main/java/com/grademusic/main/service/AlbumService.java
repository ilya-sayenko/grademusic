package com.grademusic.main.service;

import com.grademusic.main.model.Album;

import java.util.List;

public interface AlbumService {

    List<Album> findAlbumsByName(String album);

    Album findAlbumById(String id);
}
