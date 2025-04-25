package com.grademusic.main.controller;

import com.grademusic.main.controller.model.AlbumResponse;
import com.grademusic.main.controller.model.AuditionDateUpdateRequest;
import com.grademusic.main.controller.model.AlbumGradeSearchRequest;
import com.grademusic.main.controller.model.AlbumGradeResponse;
import com.grademusic.main.controller.model.UserStatisticsResponse;
import com.grademusic.main.controller.model.UserWishlistRequest;
import com.grademusic.main.mapper.AlbumMapper;
import com.grademusic.main.mapper.StatisticsMapper;
import com.grademusic.main.mapper.GradeMapper;
import com.grademusic.main.model.User;
import com.grademusic.main.service.GradeService;
import com.grademusic.main.service.ProfileService;
import com.grademusic.main.service.StatisticsService;
import com.grademusic.main.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/grade-music/main/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final StatisticsService statisticsService;

    private final ProfileService profileService;

    private final GradeService gradeService;

    private final StatisticsMapper statisticsMapper;

    private final GradeMapper userGradeMapper;

    private final AlbumMapper albumMapper;

    @GetMapping("/statistics")
    public UserStatisticsResponse findUserStatistics(Authentication authentication) {
        return statisticsMapper.toResponse(
                statisticsService.findUserStatisticsById(AuthUtils.extractUser(authentication).id())
        );
    }

    @GetMapping("/grades")
    public List<AlbumGradeResponse> findUserGradesByAlbumIds(
            Authentication authentication,
            @RequestBody(required = false) AlbumGradeSearchRequest request
    ) {
        User user = AuthUtils.extractUser(authentication);
        if (request == null) {
            request = AlbumGradeSearchRequest.builder().build();
        }
        return userGradeMapper.toResponse(
                gradeService.findGrades(request.withUserId(user.id()))
        );
    }

    @PutMapping("/audition-date")
    public void updateAuditionDate(Authentication authentication, @RequestBody AuditionDateUpdateRequest request) {
        User user = AuthUtils.extractUser(authentication);
        gradeService.updateAuditionDate(user.id(), request.albumId(), request.auditionDate());
    }

    @PostMapping("/wishlist")
    public void addAlbumToWishlist(Authentication authentication, @RequestBody UserWishlistRequest request) {
        User user = AuthUtils.extractUser(authentication);
        profileService.addAlbumToWishList(user.id(), request.albumId());
    }

    @DeleteMapping("/wishlist")
    public void deleteAlbumFromWishlist(Authentication authentication, @RequestBody UserWishlistRequest request) {
        User user = AuthUtils.extractUser(authentication);
        profileService.deleteAlbumFromWishList(user.id(), request.albumId());
    }

    @GetMapping("/wishlist")
    public List<AlbumResponse> getWishlist(Authentication authentication) {
        return albumMapper.toResponse(
                profileService.findAlbumsByUserWishlist(AuthUtils.extractUser(authentication).id())
        );
    }
}
