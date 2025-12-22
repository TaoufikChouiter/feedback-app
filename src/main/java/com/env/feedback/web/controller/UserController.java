package com.env.feedback.web.controller;

import com.env.feedback.security.permission.Permission;
import com.env.feedback.web.dto.ChangePasswordDto;
import com.env.feedback.web.dto.UserDto;
import com.env.feedback.model.User;
import com.env.feedback.security.permission.Role;
import com.env.feedback.security.principal.UserPrincipal;
import com.env.feedback.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class UserController {
    private final UserService service;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService service, UserService userService, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/user/profile")
    public String myProfile(@AuthenticationPrincipal UserPrincipal principal) {
        Long id = principal.getUser().getId();
        return "redirect:/user/" + id;
    }

    @GetMapping("/user/create")
    public String create(Model model) {
        model.addAttribute("formAction", "/user/create");
        model.addAttribute("roles", Role.values());
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("editMode", false);

        return "user/form";
    }

    @PostMapping("/user/create")
    public String create(
            @Valid @ModelAttribute("userDto") UserDto userDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/user/create");
            model.addAttribute("roles", Role.values());
            model.addAttribute("editMode", false);
            return "user/form";
        }

        User user = User.builder()
                        .username(userDto.getUsername())
                        .email(userDto.getEmail())
                        .role(userDto.getRole())
                                .build();

        service.createUser(user);

        return "redirect:/user/list";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("@userSecurity.canViewUser(#id)")
    public String user(@PathVariable Long id, Model model) {
        User user = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setEnabled(user.isEnabled());

        model.addAttribute("formAction", "/user/" + id + "/update");
        model.addAttribute("roles", Role.values());
        model.addAttribute("userDto", userDto);
        model.addAttribute("editMode", true);

        return "user/form";
    }

    @PostMapping("/user/{id}/update")
    @PreAuthorize("@userSecurity.canUpdateUser(#id)")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("userDto") UserDto userForm,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/user/" + id + "/update");
            model.addAttribute("roles", Role.values());
            model.addAttribute("editMode", true);
            return "user/form";
        }

        User user = userService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setEmail(userForm.getEmail());
        user.setEnabled(userForm.getEnabled());

        if(principal.hasPermission(Permission.USER_UPDATE_ALL)) {
            user.setRole(userForm.getRole());
        }

        service.update(user);

        return principal.hasPermission(Permission.USER_UPDATE_ALL)
                ? "redirect:/user/list"
                : "redirect:/";
    }

    @GetMapping("/user/list")
    @PreAuthorize("hasAuthority(@permissions.USER_UPDATE_ALL())")
    public String listUsers(Model model) {
        model.addAttribute("users", service.findAll());
        return "user/list";
    }

    @GetMapping("/user/change-password")
    @PreAuthorize("hasAuthority(@permissions.USER_UPDATE())")
    public String changePasswordForm(@ModelAttribute ChangePasswordDto changePasswordDto, Model model) {
        return "user/change-password";
    }

    @PostMapping("/user/change-password")
    @PreAuthorize("hasAuthority(@permissions.USER_UPDATE())")
    public String changePassword(
            @Valid @ModelAttribute ChangePasswordDto changePasswordDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal principal) {

        if(!passwordEncoder.matches(changePasswordDto.getOldPassword(), principal.getPassword())) {
            bindingResult.rejectValue(
                    "oldPassword",
                    "invalid.oldPassword",
                    "Current password is incorrect"
            );
        }

        if (bindingResult.hasErrors()) {
            return "user/change-password";
        }

        service.changeCurrentUserPassword(changePasswordDto);

        return "redirect:/";
    }
}
