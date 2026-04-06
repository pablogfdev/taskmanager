package com.pablodev.taskmanager.service;

import com.pablodev.taskmanager.entity.User;
import com.pablodev.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.findByEmail("pablo@test.com"))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.register("pablo", "pablo@test.com", "1234");

        assertNotNull(user);
        assertEquals("pablo", user.getUsername());
        assertNotEquals("1234", user.getPassword()); // importante: está encriptada

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.findByEmail("pablo@test.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register("pablo", "pablo@test.com", "1234");
        });

        verify(userRepository, never()).save(any());
    }
}