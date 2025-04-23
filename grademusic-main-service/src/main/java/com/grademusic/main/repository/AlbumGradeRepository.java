package com.grademusic.main.repository;

import com.grademusic.main.entity.AlbumGrade;
import com.grademusic.main.entity.AlbumGradeId;
import com.grademusic.main.model.projection.AlbumStatisticsByGrades;
import com.grademusic.main.model.projection.UserStatisticsByGrades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumGradeRepository extends JpaRepository<AlbumGrade, AlbumGradeId> {

    @Query(value = """
            select
                count(ag.*) countOfGrades,
                avg(ag.grade) grade
            from album_grades ag
            where ag.album_id = :albumId
            """, nativeQuery = true)
    AlbumStatisticsByGrades calculateAlbumStatistics(@Param("albumId") String albumId);

    @Query(value = """
            select
                count(ag.*) countOfGrades,
                avg(ag.grade) averageGrade,
                cast(min(ag.created_at) as date) firstGradeDate,
                cast(max(ag.created_at) as date) lastGradeDate
            from album_grades ag
            where ag.user_id = :userId
            """, nativeQuery = true)
    UserStatisticsByGrades calculateUserStatistics(@Param("userId") Long userId);

    List<AlbumGrade> findByUserId(Long userId);
}
