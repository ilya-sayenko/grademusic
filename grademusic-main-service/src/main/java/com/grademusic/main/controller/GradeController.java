package com.grademusic.main.controller;

import com.grademusic.main.controller.model.AuditionDateUpdateRequest;
import com.grademusic.main.controller.model.GradeAlbumRequest;
import com.grademusic.main.model.User;
import com.grademusic.main.service.GradeService;
import com.grademusic.main.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/grade-music/main/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    public void gradeAlbum(Authentication authentication, @RequestBody GradeAlbumRequest request) {
        User user = AuthUtils.extractUser(authentication);
        gradeService.gradeAlbum(user.id(), request.albumId(), request.grade());
    }

    @PutMapping("/audition-date")
    public void updateAuditionDate(Authentication authentication, @RequestBody AuditionDateUpdateRequest request) {
        User user = AuthUtils.extractUser(authentication);
        gradeService.updateAuditionDate(user.id(), request.albumId(), request.auditionDate());
    }
}
