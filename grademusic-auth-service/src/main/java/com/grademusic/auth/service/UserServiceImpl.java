package com.grademusic.auth.service;

import com.grademusic.auth.entity.User;
import com.grademusic.auth.exception.InvalidUsernameException;
import com.grademusic.auth.exception.UserNotFoundException;
import com.grademusic.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id=%d not found", id)));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", username)));
    }
}
