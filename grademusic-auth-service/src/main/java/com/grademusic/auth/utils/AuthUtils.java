package com.grademusic.auth.utils;

import com.grademusic.auth.entity.User;
import org.springframework.security.core.Authentication;

public class AuthUtils {

    public static User extractUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
