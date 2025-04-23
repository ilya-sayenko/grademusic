package com.grademusic.main.service;

import com.grademusic.main.model.Album;
import com.grademusic.main.model.UserGrade;

import java.util.List;

public interface ProfileService {

    void addAlbumToWishList(Long userId, String albumId);

    void deleteAlbumFromWishList(Long userId, String albumId);

    List<UserGrade> findGradesByUserId(Long userId);

    List<Album> findAlbumsByUserWishlist(Long userId);
}
