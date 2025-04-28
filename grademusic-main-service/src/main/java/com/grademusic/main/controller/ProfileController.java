package com.grademusic.main.controller;

import com.grademusic.main.controller.model.AlbumGradePaginatedResponse;
import com.grademusic.main.controller.model.AlbumPaginatedResponse;
import com.grademusic.main.controller.model.AuditionDateUpdateRequest;
import com.grademusic.main.controller.model.AlbumGradeSearchRequest;
import com.grademusic.main.controller.model.PaginatedRequest;
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
    public AlbumGradePaginatedResponse findAlbumGrades(
            Authentication authentication,
            PaginatedRequest paginatedRequest,
            @RequestBody(required = false) AlbumGradeSearchRequest request
    ) {
        User user = AuthUtils.extractUser(authentication);
        if (request == null) {
            request = AlbumGradeSearchRequest.builder().build();
        }

        return userGradeMapper.toPaginatedResponse(
                gradeService.findPaginatedGrades(request.withUserId(user.id()), paginatedRequest)
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
        profileService.addAlbumToWishlist(user.id(), request.albumId());
    }

    @DeleteMapping("/wishlist")
    public void deleteAlbumFromWishlist(Authentication authentication, @RequestBody UserWishlistRequest request) {
        User user = AuthUtils.extractUser(authentication);
        profileService.deleteAlbumFromWishlist(user.id(), request.albumId());
    }

    @GetMapping("/wishlist")
    public AlbumPaginatedResponse findAlbumGrades(
            Authentication authentication,
            PaginatedRequest paginatedRequest
    ) {
        User user = AuthUtils.extractUser(authentication);

        return albumMapper.toPaginatedResponse(
                profileService.findPaginatedAlbumsByUserWishlist(user.id(), paginatedRequest)
        );
    }
}
