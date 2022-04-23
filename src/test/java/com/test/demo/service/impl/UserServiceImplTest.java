package com.test.demo.service.impl;

import com.test.demo.domain.User;
import com.test.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers() {
        var users = Collections.singletonList(new User());
        Mockito.when(userRepository.findAll()).thenReturn(users);

        var response = userService.getAllUsers();

        assertEquals(response.size(), 1);
        assertEquals(response, users);
    }

    @Test
    void getEmptyUsers() {
        var users = new ArrayList<User>();
        Mockito.when(userRepository.findAll()).thenReturn(users);

        var response = userService.getAllUsers();

        assertEquals(response.size(), 0);
        assertEquals(response, users);
    }
}
