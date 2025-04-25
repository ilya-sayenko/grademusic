package com.grademusic.main.controller;

import com.grademusic.main.controller.model.AlbumGradeDeleteRequest;
import com.grademusic.main.controller.model.AlbumGradeRequest;
import com.grademusic.main.model.User;
import com.grademusic.main.service.GradeService;
import com.grademusic.main.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/grade-music/main/albums/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    public void gradeAlbum(Authentication authentication, @RequestBody AlbumGradeRequest request) {
        User user = AuthUtils.extractUser(authentication);
        gradeService.gradeAlbum(user.id(), request.albumId(), request.grade());
    }

    @DeleteMapping
    public void deleteGrade(Authentication authentication, @RequestBody AlbumGradeDeleteRequest request) {
        User user = AuthUtils.extractUser(authentication);
        gradeService.deleteGrade(user.id(), request.albumId());
    }
}
