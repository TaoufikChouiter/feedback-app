package com.env.feedback.config;

import com.env.feedback.model.User;
import com.env.feedback.repository.UserRepository;
import com.env.feedback.security.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApplicationStartup(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${app.user.username}")
    private String username;

    @Value("${app.user.password}")
    private String password;

    @Value("${app.user.email}")
    private String email;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        userRepository.findByUsername(username).orElseGet(() -> {
            User admin = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .role(Role.ADMIN)
                    .enabled(true)
                    .passwordChangeRequired(false)
                    .build();
            return userRepository.save(admin);
        });

        System.out.printf("""
                ===================================================
                ADMIN USER CREATED
                Username: %s
                Password: %s
                PLEASE CHANGE THIS PASSWORD
                ===================================================
                %n""", username, password);
    }
}