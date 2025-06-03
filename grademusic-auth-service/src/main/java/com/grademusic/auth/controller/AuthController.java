package com.grademusic.auth.controller;

import com.grademusic.auth.controller.model.AuthRequest;
import com.grademusic.auth.controller.model.AuthResponse;
import com.grademusic.auth.controller.model.LogoutRequest;
import com.grademusic.auth.controller.model.RefreshTokenRequest;
import com.grademusic.auth.controller.model.RegisterRequest;
import com.grademusic.auth.service.AuthService;
import com.grademusic.auth.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/grade-music/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refresh(refreshTokenRequest);
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
    }

    @PostMapping("/logout")
    public void logout(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LogoutRequest logoutRequest
    ) {
        authService.logout(logoutRequest.withAccessToken(JwtUtils.extractToken(authHeader)));
    }
}
