package com.grademusic.auth.service;

import com.grademusic.auth.entity.User;
import com.grademusic.auth.exception.InvalidUsernameException;
import com.grademusic.auth.exception.UserNotFoundException;
import com.grademusic.auth.repository.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldCreate() {
        User user = Instancio.create(User.class);
        when(userRepository.existsByUsername(eq(user.getUsername()))).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> userService.create(user));
    }

    @Test
    public void shouldThrowWhileCreatingIfExistsByUsername() {
        User user = Instancio.create(User.class);
        when(userRepository.existsByUsername(eq(user.getUsername()))).thenReturn(true);

        assertThrows(InvalidUsernameException.class, () -> userService.create(user));
    }

    @Test
    public void shouldFindById() {
        User user = Instancio.create(User.class);
        when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.of(user));

        assertThat(userService.findById(user.getId()))
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    public void shouldThrowsWhenNotExistsById() {
        long userId = 1L;
        when(userRepository.findById(eq(userId))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    public void findByUsername() {
        User user = Instancio.create(User.class);
        when(userRepository.findByUsername(eq(user.getUsername()))).thenReturn(Optional.of(user));

        assertThat(userService.findByUsername(user.getUsername()))
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    public void shouldThrowsWhenNotExistsByUsername() {
        String username = "test";
        when(userRepository.findByUsername(eq(username))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByUsername(username));
    }
}