package com.grademusic.main.repository;

import com.grademusic.main.entity.AlbumGrade;
import com.grademusic.main.entity.AlbumGradeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumGradeRepository extends JpaRepository<AlbumGrade, AlbumGradeId> {
}
