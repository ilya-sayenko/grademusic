package com.grademusic.main.controller;

import com.grademusic.main.controller.model.AlbumResponse;
import com.grademusic.main.model.User;
import com.grademusic.main.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/grade-music/main/albums")
@RequiredArgsConstructor
public class AlbumController {

    @GetMapping
    public List<AlbumResponse> findAlbums(Authentication authentication) {
        User user = AuthUtils.extractUser(authentication);

        return null;
    }
}
