package com.grademusic.main.utils;

import com.grademusic.main.model.User;
import org.springframework.security.core.Authentication;

public class AuthUtils {

    public static User extractUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
