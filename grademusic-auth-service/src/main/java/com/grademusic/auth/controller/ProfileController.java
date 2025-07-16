package com.grademusic.auth.controller;

import com.grademusic.auth.controller.model.UserResponse;
import com.grademusic.auth.entity.User;
import com.grademusic.auth.mapper.UserMapper;
import com.grademusic.auth.service.UserService;
import com.grademusic.auth.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/grade-music/auth")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping("/profile")
    public UserResponse profile(Authentication authentication) {
        User user = AuthUtils.extractUser(authentication);

        return userMapper.toResponse(user);
    }
}
