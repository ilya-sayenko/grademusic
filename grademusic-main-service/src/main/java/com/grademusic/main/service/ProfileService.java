package com.grademusic.main.service;

import com.grademusic.main.controller.model.PaginatedRequest;
import com.grademusic.main.model.Album;
import org.springframework.data.domain.Page;

public interface ProfileService {

    void addAlbumToWishList(Long userId, String albumId);

    void deleteAlbumFromWishList(Long userId, String albumId);

    Page<Album> findPaginatedAlbumsByUserWishlist(Long userId, PaginatedRequest paginatedRequest);
}
