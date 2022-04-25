package com.assignment.demo.it.controller;

import com.assignment.demo.controller.UserController;
import com.assignment.demo.domain.User;
import com.assignment.demo.it.AbstractControllerIT;
import com.assignment.demo.it.AbstractIT;
import com.assignment.demo.service.UserService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerIT extends AbstractControllerIT {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAll() throws Exception {
        var name = "Test name";
        var surname = "Test surname";
        var customerId = UUID.randomUUID();
        var user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setCustomerId(customerId);

        var users = Collections.singletonList(user);

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is(name)))
                .andExpect(jsonPath("$[0].surname", Matchers.is(surname)))
                .andExpect(jsonPath("$[0].customerId", Matchers.is(customerId.toString())));
    }
}
