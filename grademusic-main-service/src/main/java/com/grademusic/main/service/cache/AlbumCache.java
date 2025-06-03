package com.grademusic.main.service.cache;

import com.grademusic.main.model.Album;

import java.util.List;
import java.util.Optional;

public interface AlbumCache {

    Optional<Album> findById(String albumId);

    List<Album> findAllById(List<String> albumIds);

    void put(Album album);
}
