package com.grademusic.main.service;

import com.grademusic.main.entity.WishlistItem;
import com.grademusic.main.entity.WishlistItemId;
import com.grademusic.main.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    public void shouldAddAlbumToWishList() {
        long userId = 1;
        String albumId = "album";
        profileService.addAlbumToWishList(userId, albumId);

        verify(wishlistRepository, atLeastOnce()).save(any(WishlistItem.class));
    }

    @Test
    public void shouldDeleteAlbumFromWishList() {
        long userId = 1;
        String albumId = "album";
        profileService.deleteAlbumFromWishList(userId, albumId);

        verify(wishlistRepository, atLeastOnce()).deleteById(any(WishlistItemId.class));
    }
}