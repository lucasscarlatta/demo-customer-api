package com.test.demo.it;

import com.test.demo.domain.User;
import com.test.demo.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserRepositoryIT extends AbstractIT {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void after() {
        userRepository.deleteAll();
    }

    @Test
    public void save_and_read_User() {
        var name = "Test name";
        var surname = "Test surname";
        var uuid = UUID.randomUUID();
        var user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setCustomerId(uuid);

        userRepository.save(user);

        List<User> users = userRepository.findAll();

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

    @AfterEach
    public void cleanUpEach(){
        System.out.println("After Each cleanUpEach() method called");
    }
}
