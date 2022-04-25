package com.assignment.demo.repository;

import com.assignment.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByCustomerId(UUID customerId);
}
