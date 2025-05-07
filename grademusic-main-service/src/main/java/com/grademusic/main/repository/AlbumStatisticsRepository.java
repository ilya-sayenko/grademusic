package com.grademusic.main.repository;

import com.grademusic.main.entity.AlbumStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumStatisticsRepository extends JpaRepository<AlbumStatistics, String> {
}
