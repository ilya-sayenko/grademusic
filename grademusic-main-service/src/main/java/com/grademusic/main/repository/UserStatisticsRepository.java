package com.grademusic.main.repository;

import com.grademusic.main.entity.UserStatistics;
import com.grademusic.main.model.projection.UserStatisticsByGrades;
import com.grademusic.main.model.projection.UserStatisticsByReviews;
import com.grademusic.main.model.projection.UserStatisticsByWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {

    @Query(value = """
            insert into user_statistics(
                user_id,
                average_grade,
                count_of_grades,
                first_grade_at,
                last_grade_at,
                updated_at
            )
            values(
                :#{#statistics.userId},
                :#{#statistics.averageGrade},
                :#{#statistics.countOfGrades},
                :#{#statistics.firstGradeDate},
                :#{#statistics.lastGradeDate},
                now()
            )
            on conflict(user_id) do update set
                average_grade = :#{#statistics.averageGrade},
                count_of_grades = :#{#statistics.countOfGrades},
                first_grade_at = :#{#statistics.firstGradeDate},
                last_grade_at = :#{#statistics.lastGradeDate},
                updated_at = now()
            returning *
            """, nativeQuery = true)
    UserStatistics saveStatisticsByGrades(UserStatisticsByGrades statistics);

    @Query(value = """
            insert into user_statistics(
                user_id,
                count_of_wishlist_items,
                updated_at
            )
            values(
                :#{#statistics.userId},
                :#{#statistics.countOfWishlistItems},
                now()
            )
            on conflict(user_id) do update set
                count_of_wishlist_items = :#{#statistics.countOfWishlistItems},
                updated_at = now()
            returning *
            """, nativeQuery = true)
    UserStatistics saveStatisticsByWishlist(UserStatisticsByWishlist statistics);

    @Query(value = """
            insert into user_statistics(
                user_id,
                count_of_reviews,
                updated_at
            )
            values(
                :#{#statistics.userId},
                :#{#statistics.countOfReviews},
                now()
            )
            on conflict(user_id) do update set
                count_of_reviews = :#{#statistics.countOfReviews},
                updated_at = now()
            returning *
            """, nativeQuery = true)
    UserStatistics saveStatisticsByReviews(UserStatisticsByReviews statistics);
}
