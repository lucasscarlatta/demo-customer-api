package com.assignment.demo.service;

import com.assignment.demo.domain.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<User> getAllUsers();

    User getUserByCustomerId(UUID customerId);
}
