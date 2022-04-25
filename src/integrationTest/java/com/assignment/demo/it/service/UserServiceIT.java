package com.assignment.demo.it.service;

import com.assignment.demo.domain.User;
import com.assignment.demo.exception.NotFoundException;
import com.assignment.demo.it.AbstractServiceIT;
import com.assignment.demo.repository.UserRepository;
import com.assignment.demo.service.UserService;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceIT extends AbstractServiceIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void saveAndReadUser() {
        var name = "Test name";
        var surname = "Test surname";
        var uuid = UUID.randomUUID();
        var user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setCustomerId(uuid);

        userRepository.save(user);

        var users = userService.getAllUsers();

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertAll("User list", () -> {
            assertEquals(users.size(), 1);
            assertAll("First user", () -> {
                var firstUser = users.get(0);
                assertEquals(name, firstUser.getName());
                assertEquals(surname, firstUser.getSurname());
                assertEquals(uuid, firstUser.getCustomerId());
            });
        });
    }

    @Test
    public void findUserById() {
        var name = "Test name";
        var surname = "Test surname";
        var uuid = UUID.randomUUID();
        var user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setCustomerId(uuid);

        userRepository.save(user);

        var userResponse = userService.getUserByCustomerId(uuid);

        assertNotNull(userResponse);
        assertAll("User response", () -> {
            assertEquals(name, userResponse.getName());
            assertEquals(surname, userResponse.getSurname());
            assertEquals(uuid, userResponse.getCustomerId());
        });
    }

    @Test
    public void findUserByIdNotFound() {
        var uuid = UUID.randomUUID();

        var thrown = assertThrows(NotFoundException.class, () -> userService.getUserByCustomerId(uuid));

        Assertions.assertEquals(String.format("Customer not found. [CustomerId: %s]", uuid), thrown.getMessage());
    }

    @After
    public void after() {
        userRepository.deleteAll();
    }
}
