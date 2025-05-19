package com.grademusic.main.repository;

import com.grademusic.main.entity.AlbumGrade;
import com.grademusic.main.entity.AlbumGradeId;
import com.grademusic.main.model.projection.AlbumStatisticsByGrades;
import com.grademusic.main.model.projection.UserStatisticsByGrades;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumGradeRepository extends JpaRepository<AlbumGrade, AlbumGradeId> {

    @Query(value = """
            select
                albumId,
                count(ag.*) countOfGrades,
                avg(coalesce(ag.grade, 0)) grade
            from unnest(array[ :albumIds ]) albumId
            left join album_grades ag
                   on ag.album_id = albumId
            group by albumId
            """, nativeQuery = true)
    List<AlbumStatisticsByGrades> calculateAlbumsStatistics(List<String> albumIds);

    @Query(value = """
            select
                userId,
                count(ag.*) countOfGrades,
                avg(coalesce(ag.grade, 0)) averageGrade,
                cast(min(ag.created_at) as date) firstGradeDate,
                cast(max(ag.created_at) as date) lastGradeDate
            from unnest(array[ :userIds ]) userId
            left join album_grades ag
                   on ag.user_id = userId
            group by userId
            """, nativeQuery = true)
    List<UserStatisticsByGrades> calculateUsersStatistics(List<Long> userIds);

    Page<AlbumGrade> findByUserId(Long userId, Pageable pageable);

    Page<AlbumGrade> findByUserIdAndAlbumIdIn(Long userId, List<String> albumIds, Pageable pageable);
}
