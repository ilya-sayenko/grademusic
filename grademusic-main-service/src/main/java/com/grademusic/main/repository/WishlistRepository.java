package com.grademusic.main.repository;

import com.grademusic.main.entity.WishlistItem;
import com.grademusic.main.entity.WishlistItemId;
import com.grademusic.main.model.projection.AlbumStatisticsByWishlist;
import com.grademusic.main.model.projection.UserStatisticsByWishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, WishlistItemId> {

    @Query(value = """
            select
                albumId,
                count(uw.*) countOfWishlistItems
            from unnest(array[ :albumIds ]) albumId
            left join user_wishlists uw
                   on uw.album_id = albumId
            group by albumId
            """, nativeQuery = true)
    List<AlbumStatisticsByWishlist> calculateAlbumsStatistics(List<String> albumIds);

    @Query(value = """
            select
                userId,
                count(uw.*) countOfWishlistItems
            from unnest(array[ :userIds ]) userId
            left join user_wishlists uw
                   on uw.user_id = userId
            group by userId
            """, nativeQuery = true)
    List<UserStatisticsByWishlist> calculateUsersStatistics(List<Long> userIds);

    Page<WishlistItem> findByUserId(Long userId, Pageable pageable);
}
