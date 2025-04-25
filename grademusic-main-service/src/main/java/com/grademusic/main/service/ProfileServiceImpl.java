package com.grademusic.main.service;

import com.grademusic.main.entity.WishlistItem;
import com.grademusic.main.entity.WishlistItemId;
import com.grademusic.main.model.Album;
import com.grademusic.main.repository.AlbumGradeRepository;
import com.grademusic.main.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final WishlistRepository wishlistRepository;

    private final AlbumGradeRepository albumGradeRepository;

    private final AlbumService albumService;

    @Override
    public void addAlbumToWishList(Long userId, String albumId) {
        wishlistRepository.save(
                WishlistItem.builder()
                        .userId(userId)
                        .albumId(albumId)
                        .build()
        );
    }

    @Override
    public void deleteAlbumFromWishList(Long userId, String albumId) {
        wishlistRepository.deleteById(WishlistItemId.builder().userId(userId).albumId(albumId).build());
    }

    @Override
    public List<Album> findAlbumsByUserWishlist(Long userId) {
        List<String> albumIds = wishlistRepository.findByUserId(userId).stream()
                .map(WishlistItem::getAlbumId)
                .toList();

        return albumService.findAllAlbumsById(albumIds);
    }
}
