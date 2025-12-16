package com.env.feedback.service;

import com.env.feedback.model.User;
import com.env.feedback.repository.UserRepository;
import com.env.feedback.security.PasswordGenerator;
import com.env.feedback.security.Role;
import com.env.feedback.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repo;

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @PreAuthorize("hasAuthority(@permissions.FEEDBACK_UPDATE())")
    public List<User> findAssignableUsers() {
        return repo.findByRole(Role.FEEDBACK_MANAGER);
    }

    @PreAuthorize("hasAuthority(@permissions.USER_READ_ALL())")
    public List<User> findAll() {
        return repo.findAll();
    }

    public void createUser(User user) {
        logger.info("Creating user: {}", user.getUsername());
        String rawPassword = PasswordGenerator.generate(12);
        user.setPassword(passwordEncoder.encode(rawPassword));

        repo.save(user);

        emailService.sendResetPassword(user.getEmail(), rawPassword);
    }

    public UserDetails loadUserByUsername(String username) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new UserPrincipal(user);
    }

    @PreAuthorize("hasAuthority(@permissions.USER_UPDATE())")
    public void changeCurrentUserPassword(String password) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        User user = userPrincipal.getUser();
        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordChangeRequired(false);

        logger.info("User password updated: {}", user.getUsername());

        repo.save(user);
    }

    @PreAuthorize("@userSecurity.canViewUser(#id)")
    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }

    @PreAuthorize("@userSecurity.canUpdateUser(#user)")
    public void update(User user) {
        logger.info("Updating user: {}", user.getUsername());

        repo.save(user);
    }
}