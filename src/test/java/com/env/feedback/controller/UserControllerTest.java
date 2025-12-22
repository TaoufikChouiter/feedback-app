package com.env.feedback.controller;

import com.env.feedback.model.User;
import com.env.feedback.security.permission.Permission;
import com.env.feedback.security.permission.Role;
import com.env.feedback.security.principal.UserPrincipal;
import com.env.feedback.service.UserService;
import com.env.feedback.web.controller.UserController;
import com.env.feedback.web.dto.ChangePasswordDto;
import com.env.feedback.web.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService service;

    @Mock
    private UserService userService; // used for updates

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private User testUser;
    private UserPrincipal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user1");
        testUser.setEmail("user1@example.com");
        testUser.setRole(Role.ADMIN);
        testUser.setEnabled(true);
        testUser.setPassword("encodedPassword");

        principal = mock(UserPrincipal.class);
        when(principal.getUser()).thenReturn(testUser);
        when(principal.hasPermission(Permission.USER_UPDATE_ALL)).thenReturn(true);
    }

    @Test
    void login_shouldReturnLoginView() {
        String view = userController.login();
        assertEquals("user/login", view);
    }

    @Test
    void myProfile_shouldRedirectToUserProfile() {
        when(principal.getUser()).thenReturn(testUser);
        String redirect = userController.myProfile(principal);
        assertEquals("redirect:/user/1", redirect);
    }

    @Test
    void createUser_get_shouldPopulateModel() {
        String view = userController.create(model);

        assertEquals("user/form", view);
        verify(model).addAttribute(eq("formAction"), eq("/user/create"));
        verify(model).addAttribute(eq("roles"), any());
        verify(model).addAttribute(eq("userDto"), any());
        verify(model).addAttribute(eq("editMode"), eq(false));
    }

    @Test
    void createUser_post_valid_shouldCallServiceAndRedirect() {
        UserDto dto = new UserDto();
        dto.setUsername("newuser");
        dto.setEmail("new@example.com");
        dto.setRole(Role.USER);

        when(bindingResult.hasErrors()).thenReturn(false);

        String redirect = userController.create(dto, bindingResult, model);

        assertEquals("redirect:/user/list", redirect);
        verify(service).createUser(any(User.class));
    }

    @Test
    void createUser_post_invalid_shouldReturnForm() {
        UserDto dto = new UserDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = userController.create(dto, bindingResult, model);

        assertEquals("user/form", view);
        verify(model).addAttribute(eq("formAction"), eq("/user/create"));
        verify(model).addAttribute(eq("roles"), any());
        verify(model).addAttribute(eq("editMode"), eq(false));
    }

    @Test
    void getUser_notFound_shouldThrow404() {
        when(service.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userController.user(1L, model));
    }

    @Test
    void updateUser_invalid_shouldReturnForm() {
        UserDto dto = new UserDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = userController.update(1L, dto, bindingResult, model, principal);

        assertEquals("user/form", view);
        verify(model).addAttribute(eq("formAction"), eq("/user/1/update"));
        verify(model).addAttribute(eq("roles"), any());
        verify(model).addAttribute(eq("editMode"), eq(true));
        verify(service, never()).update(any());
    }

    @Test
    void updateUser_notFound_shouldThrow404() {
        UserDto dto = new UserDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> userController.update(1L, dto, bindingResult, model, principal));
    }

    @Test
    void changePasswordForm_shouldReturnView() {
        ChangePasswordDto dto = new ChangePasswordDto();
        String view = userController.changePasswordForm(dto, model);

        assertEquals("user/change-password", view);
    }

    @Test
    void changePassword_invalidOldPassword_shouldReturnForm() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setOldPassword("wrong");
        dto.setPassword("newPassword123!");
        dto.setConfirmPassword("newPassword123!");

        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = userController.changePassword(dto, bindingResult, principal);

        assertEquals("user/change-password", view);
        verify(service, never()).changeCurrentUserPassword(any());
    }
}
