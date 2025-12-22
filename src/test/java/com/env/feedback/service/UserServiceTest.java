package com.env.feedback.service;

import com.env.feedback.model.User;
import com.env.feedback.repository.UserRepository;
import com.env.feedback.security.password.PasswordGeneratorService;
import com.env.feedback.security.permission.Role;
import com.env.feedback.security.principal.UserPrincipal;
import com.env.feedback.web.dto.ChangePasswordDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository repo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordGeneratorService passwordGeneratorService;

    @Mock
    private EmailService emailService;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user1");
        testUser.setEmail("user1@example.com");
        testUser.setRole(Role.FEEDBACK_MANAGER);
        testUser.setPassword("encodedOldPassword");

        // Mock SecurityContextHolder with principal
        UserPrincipal principal = mock(UserPrincipal.class);

        // Lenient stub to avoid unnecessary stubbing exception
        lenient().when(principal.getUser()).thenReturn(testUser);

        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(principal, null, "ROLE_FEEDBACK_MANAGER");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // ============================
    // Test: createUser
    // ============================
    @Test
    void createUser_shouldGeneratePasswordEncodeAndSendEmail() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setEmail("test@example.com");

        when(passwordGeneratorService.generate(12)).thenReturn("rawPassword");
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        userService.createUser(newUser);

        assertEquals("encodedPassword", newUser.getPassword());
        verify(repo).save(newUser);
        verify(emailService).sendResetPassword("test@example.com", "rawPassword");
    }

    // ============================
    // Test: update
    // ============================
    @Test
    void update_shouldSaveUser() {
        testUser.setEmail("newemail@example.com");

        userService.update(testUser);

        verify(repo).save(testUser);
    }

    // ============================
    // Test: changeCurrentUserPassword
    // ============================
    @Test
    void changeCurrentUserPassword_shouldEncodeNewPasswordAndSave() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setOldPassword("oldPassword");
        dto.setPassword("newPassword22");
        dto.setConfirmPassword("newPassword22");

        when(passwordEncoder.encode("newPassword22")).thenReturn("encodedNewPassword");

        userService.changeCurrentUserPassword(dto);

        assertEquals("encodedNewPassword", testUser.getPassword());
        assertFalse(testUser.isPasswordChangeRequired());
        verify(repo).save(testUser);
    }

    // ============================
    // Test: findAssignableUsers
    // ============================
    @Test
    void findAssignableUsers_shouldReturnOnlyFeedbackManagers() {
        User u1 = new User();
        u1.setRole(Role.FEEDBACK_MANAGER);

        User u2 = new User();
        u2.setRole(Role.FEEDBACK_MANAGER);

        when(repo.findByRole(Role.FEEDBACK_MANAGER)).thenReturn(List.of(u1, u2));

        List<User> result = userService.findAssignableUsers();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(u -> u.getRole() == Role.FEEDBACK_MANAGER));
    }

    @Test
    void findAssignableUsers_noUsers_shouldReturnEmptyList() {
        when(repo.findByRole(Role.FEEDBACK_MANAGER)).thenReturn(List.of());

        List<User> result = userService.findAssignableUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ============================
    // Test: findById
    // ============================
    @Test
    void findById_shouldReturnOptionalUser() {
        when(repo.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> userOpt = userService.findById(1L);

        assertTrue(userOpt.isPresent());
        assertEquals(testUser, userOpt.get());
    }

    @Test
    void findById_userNotFound_shouldReturnEmptyOptional() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(999L);

        assertTrue(result.isEmpty());
    }

    // ============================
    // Test: loadUserByUsername
    // ============================
    @Test
    void loadUserByUsername_shouldReturnUserPrincipal() {
        when(repo.findByUsername("user1")).thenReturn(Optional.of(testUser));

        UserPrincipal principal = (UserPrincipal) userService.loadUserByUsername("user1");

        assertEquals("user1", principal.getUsername());
    }

    @Test
    void loadUserByUsername_customPrincipalFields() {
        testUser.setEmail("custom@example.com");
        when(repo.findByUsername("user1")).thenReturn(Optional.of(testUser));

        UserPrincipal principal = (UserPrincipal) userService.loadUserByUsername("user1");

        assertEquals("custom@example.com", principal.getUser().getEmail());
    }

    @Test
    void loadUserByUsername_userNotFound_shouldThrowException() {
        when(repo.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("unknown"));
    }
}