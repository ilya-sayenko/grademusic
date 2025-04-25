package com.grademusic.main.service;

import com.grademusic.main.controller.model.AlbumSearchRequest;
import com.grademusic.main.model.Album;

import java.util.List;

public interface AlbumService {

    List<Album> findAlbums(AlbumSearchRequest albumSearchRequest);

    List<Album> findAlbumsByName(String album);

    Album findAlbumById(String id);

    List<Album> findAllAlbumsById(List<String> ids);
}
