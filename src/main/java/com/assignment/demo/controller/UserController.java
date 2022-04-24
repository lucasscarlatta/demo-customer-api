package com.assignment.demo.controller;

import com.assignment.demo.domain.User;
import com.assignment.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    List<User> getAllUsers() {
        var stopWatch = new Slf4JStopWatch("Get all users");
        try {
            return userService.getAllUsers();
        } finally {
            stopWatch.stop();
        }
    }
}
