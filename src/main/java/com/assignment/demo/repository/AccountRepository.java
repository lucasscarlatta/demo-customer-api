package com.assignment.demo.repository;

import com.assignment.demo.domain.Account;
import com.assignment.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    boolean existsById(UUID id);
}
