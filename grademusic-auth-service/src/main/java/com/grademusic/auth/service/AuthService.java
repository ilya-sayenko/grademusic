package com.grademusic.auth.service;

import com.grademusic.auth.controller.model.AuthRequest;
import com.grademusic.auth.controller.model.AuthResponse;
import com.grademusic.auth.controller.model.LogoutRequest;
import com.grademusic.auth.controller.model.RefreshTokenRequest;
import com.grademusic.auth.controller.model.RegisterRequest;

public interface AuthService {

    AuthResponse login(AuthRequest authRequest);

    void register(RegisterRequest registerRequest);

    AuthResponse refresh(RefreshTokenRequest refreshTokenRequest);

    void logout(LogoutRequest logoutRequest);
}
