package com.grademusic.main.service.cache;

import com.grademusic.main.entity.AlbumStatistics;

import java.util.List;
import java.util.Optional;

public interface AlbumStatisticsCache {

    Optional<AlbumStatistics> findById(String albumId);

    List<AlbumStatistics> findAllById(List<String> albumId);

    void put(AlbumStatistics albumStatistics);
}
