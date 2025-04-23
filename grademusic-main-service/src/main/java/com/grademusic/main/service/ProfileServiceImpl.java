package com.grademusic.main.service;

import com.grademusic.main.entity.AlbumGrade;
import com.grademusic.main.entity.WishlistItem;
import com.grademusic.main.entity.WishlistItemId;
import com.grademusic.main.model.Album;
import com.grademusic.main.model.UserGrade;
import com.grademusic.main.repository.AlbumGradeRepository;
import com.grademusic.main.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<UserGrade> findGradesByUserId(Long userId) {
        List<AlbumGrade> albumGrades = albumGradeRepository.findByUserId(userId);
        List<String> albumIds = albumGrades.stream()
                .map(AlbumGrade::getAlbumId)
                .toList();
        Map<String, Album> albums = albumService.findAllAlbumsById(albumIds).stream()
                .collect(Collectors.toMap(Album::getId, Function.identity()));

        List<UserGrade> userGrades = new ArrayList<>();
        for (AlbumGrade albumGrade : albumGrades) {
            userGrades.add(
                    UserGrade.builder()
                            .userId(userId)
                            .album(albums.get(albumGrade.getAlbumId()))
                            .userGrade(albumGrade.getGrade())
                            .createDate(albumGrade.getCreateDate())
                            .build()
            );
        }

        return userGrades;
    }

    @Override
    public List<Album> findAlbumsByUserWishlist(Long userId) {
        List<String> albumIds = wishlistRepository.findByUserId(userId).stream()
                .map(WishlistItem::getAlbumId)
                .toList();

        return albumService.findAllAlbumsById(albumIds);
    }
}
