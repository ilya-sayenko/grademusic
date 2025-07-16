package com.grademusic.main.controller;

import com.grademusic.main.controller.model.AlbumResponse;
import com.grademusic.main.controller.model.AlbumSearchRequest;
import com.grademusic.main.controller.model.AlbumStatisticsResponse;
import com.grademusic.main.mapper.AlbumMapper;
import com.grademusic.main.mapper.StatisticsMapper;
import com.grademusic.main.service.AlbumService;
import com.grademusic.main.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/grade-music/main/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    private final StatisticsService statisticsService;

    private final AlbumMapper albumMapper;

    private final StatisticsMapper statisticsMapper;

    @GetMapping
    public List<AlbumResponse> findAlbums(@RequestBody AlbumSearchRequest albumSearchRequest) {
        return albumMapper.toResponse(albumService.findAlbums(albumSearchRequest));
    }

    @GetMapping("/{id}")
    public AlbumResponse findAlbumById(@PathVariable("id") String id) {
        return albumMapper.toResponse(albumService.findAlbumById(id));
    }

    @GetMapping("/{id}/statistics")
    public AlbumStatisticsResponse findAlbumStatisticsById(@PathVariable("id") String id) {
        return statisticsMapper.toResponse(statisticsService.findAlbumStatisticsById(id));
    }
}
