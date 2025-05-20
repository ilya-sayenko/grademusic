package com.grademusic.main.repository;

import com.grademusic.main.entity.AlbumReview;
import com.grademusic.main.model.projection.AlbumStatisticsByReviews;
import com.grademusic.main.model.projection.UserStatisticsByReviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumReviewRepository extends JpaRepository<AlbumReview, Long> {

    @Query(value = """
            select
                albumId,
                count(ar.*) countOfReviews
            from unnest(array[ :albumIds ]) albumId
            left join album_reviews ar
                   on ar.album_id = albumId
            group by albumId
            """, nativeQuery = true)
    List<AlbumStatisticsByReviews> calculateAlbumsStatistics(List<String> albumIds);

    @Query(value = """
            select
                userId,
                count(ar.*) countOfReviews
            from unnest(array[ :userIds ]) userId
            left join album_reviews ar
                   on ar.user_id = userId
            group by userId
            """, nativeQuery = true)
    List<UserStatisticsByReviews> calculateUsersStatistics(List<Long> userIds);

    Page<AlbumReview> findByAlbumId(String albumId, Pageable pageable);
}
