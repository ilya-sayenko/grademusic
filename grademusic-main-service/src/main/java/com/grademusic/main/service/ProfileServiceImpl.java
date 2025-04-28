package com.grademusic.main.service;

import com.grademusic.main.controller.model.PaginatedRequest;
import com.grademusic.main.entity.WishlistItem;
import com.grademusic.main.entity.WishlistItemId;
import com.grademusic.main.exception.WishlistItemExistsException;
import com.grademusic.main.model.Album;
import com.grademusic.main.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final WishlistRepository wishlistRepository;

    private final AlbumService albumService;

    @Override
    public void addAlbumToWishList(Long userId, String albumId) {
       if (wishlistRepository.existsById(calculateWishlistItemId(userId, albumId))) {
           throw new WishlistItemExistsException(String.format("Album id=%s has been already added to wishlist", albumId));
       }
        wishlistRepository.save(
                WishlistItem.builder()
                        .userId(userId)
                        .albumId(albumId)
                        .build()
        );
    }

    @Override
    public void deleteAlbumFromWishList(Long userId, String albumId) {
        wishlistRepository.deleteById(calculateWishlistItemId(userId, albumId));
    }

    @Override
    public Page<Album> findPaginatedAlbumsByUserWishlist(Long userId, PaginatedRequest paginatedRequest) {
        PageRequest pageRequest = PageRequest.of(
                paginatedRequest.page(),
                paginatedRequest.perPage(),
                Sort.by(Sort.Direction.DESC, "createDate")
        );
        Page<WishlistItem> wishlistItems = wishlistRepository.findByUserId(userId, pageRequest);
        List<String> albumIds = wishlistItems.stream()
                .map(WishlistItem::getAlbumId)
                .toList();
        List<Album> albums = albumService.findAllAlbumsById(albumIds);

        return new PageImpl<>(albums, pageRequest, wishlistItems.getTotalElements());
    }

    private WishlistItemId calculateWishlistItemId(Long userId, String albumId) {
        return WishlistItemId.builder()
                .userId(userId)
                .albumId(albumId)
                .build();
    }
}
