package com.assignment.demo.service.impl;

import com.assignment.demo.domain.User;
import com.assignment.demo.exception.NotFoundException;
import com.assignment.demo.repository.UserRepository;
import com.assignment.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j(topic = "User service")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByCustomerId(UUID customerId) {
        return userRepository.findByCustomerId(customerId).orElseThrow(() -> {
            var message = String.format("Customer not found. [CustomerId: %s]", customerId);
            log.error(message);
            throw new NotFoundException(message);
        });
    }
}
