package com.grademusic.main.repository;

import com.grademusic.main.entity.AlbumStatistics;
import com.grademusic.main.model.projection.AlbumStatisticsByGrades;
import com.grademusic.main.model.projection.AlbumStatisticsByReviews;
import com.grademusic.main.model.projection.AlbumStatisticsByWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumStatisticsRepository extends JpaRepository<AlbumStatistics, String> {

    @Query(value = """
            insert into album_statistics(
                album_id,
                grade,
                count_of_grades,
                updated_at
            )
            values(
                :#{#statistics.albumId},
                :#{#statistics.grade},
                :#{#statistics.countOfGrades},
                now()
            )
            on conflict(album_id) do update set
                grade = :#{#statistics.grade},
                count_of_grades = :#{#statistics.countOfGrades},
                updated_at = now()
            returning *
            """, nativeQuery = true)
    AlbumStatistics saveStatisticsByGrades(AlbumStatisticsByGrades statistics);

    @Query(value = """
            insert into album_statistics(
                album_id,
                count_of_wishlist_items,
                updated_at
            )
            values(
                :#{#statistics.albumId},
                :#{#statistics.countOfWishlistItems},
                now()
            )
            on conflict(album_id) do update set
                count_of_wishlist_items = :#{#statistics.countOfWishlistItems},
                updated_at = now()
            returning *
            """, nativeQuery = true)
    AlbumStatistics saveStatisticsByWishlist(AlbumStatisticsByWishlist statistics);

    @Query(value = """
            insert into album_statistics(
                album_id,
                count_of_reviews,
                updated_at
            )
            values(
                :#{#statistics.albumId},
                :#{#statistics.countOfReviews},
                now()
            )
            on conflict(album_id) do update set
                count_of_reviews = :#{#statistics.countOfReviews},
                updated_at = now()
            returning *
            """, nativeQuery = true)
    AlbumStatistics saveStatisticsByReviews(AlbumStatisticsByReviews statistics);
}
