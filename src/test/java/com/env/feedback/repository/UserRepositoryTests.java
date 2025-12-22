package com.env.feedback.repository;

import com.env.feedback.model.User;
import com.env.feedback.security.permission.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        // Basic context load test
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("secret");
        user.setEnabled(true);
        user.setRole(Role.USER);
        userRepository.save(user);

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void testFindUserByUsername() {
        User user = new User();
        user.setUsername("findme");
        user.setEmail("findme@example.com");
        user.setPassword("secret");
        user.setEnabled(true);
        user.setRole(Role.USER);
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("findme");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("findme@example.com");
    }

    @Test
    void testUpdateUserPassword() {
        User user = new User();
        user.setUsername("updatepass");
        user.setEmail("updatepass@example.com");
        user.setPassword("oldpass");
        user.setEnabled(true);
        user.setRole(Role.USER);
        userRepository.save(user);

        user.setPassword("newpass");
        userRepository.save(user);

        User updated = userRepository.findByUsername("updatepass").get();
        assertThat(updated.getPassword()).isEqualTo("newpass");
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setUsername("todelete");
        user.setEmail("todelete@example.com");
        user.setPassword("secret");
        user.setEnabled(true);
        user.setRole(Role.USER);
        userRepository.save(user);

        userRepository.delete(user);
        assertThat(userRepository.findByUsername("todelete")).isEmpty();
    }
}