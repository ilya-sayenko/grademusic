package com.grademusic.auth.service;

import com.grademusic.auth.entity.User;

public interface UserService {

    User create(User user);

    User findById(long id);

    User findByUsername(String username);
}
