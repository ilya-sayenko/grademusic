package com.grademusic.main.controller;

import com.grademusic.main.controller.model.AlbumSearchResponse;
import com.grademusic.main.controller.model.AlbumSearchRequest;
import com.grademusic.main.mapper.AlbumMapper;
import com.grademusic.main.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/grade-music/main/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    private final AlbumMapper albumMapper;

    @GetMapping
    public List<AlbumSearchResponse> findAlbums(AlbumSearchRequest albumSearchRequest) {
        return albumMapper.toResponse(albumService.findAlbumsByName(albumSearchRequest.album()));
    }

    @GetMapping("/{id}")
    public AlbumSearchResponse findAlbumById(@PathVariable("id") String id) {
        return albumMapper.toResponse(albumService.findAlbumById(id));
    }
}
