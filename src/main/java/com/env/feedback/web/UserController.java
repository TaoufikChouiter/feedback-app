package com.env.feedback.web;

import com.env.feedback.model.User;
import com.env.feedback.security.Permission;
import com.env.feedback.security.Role;
import com.env.feedback.security.UserPrincipal;
import com.env.feedback.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
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

    @GetMapping("/user")
    public String create(Model model) {
        model.addAttribute("roles", Role.values());
        model.addAttribute("user", new User());
        model.addAttribute("editMode", false);

        return "user/form";
    }

    @PostMapping("/user")
    public String create(
            @ModelAttribute("user") User user,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("editMode", false);
            return "user/form";
        }

        service.createUser(user);

        return "redirect:/user/list";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("@userSecurity.canViewUser(#id)")
    public String user(@PathVariable Long id, Model model) {
        User user = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("roles", Role.values());
        model.addAttribute("user", user);
        model.addAttribute("editMode", true);

        return "user/form";
    }

    @PostMapping("/user/{id}/update")
    @PreAuthorize("@userSecurity.canUpdateUser(#userForm)")
    public String update(
            @PathVariable Long id,
            @ModelAttribute("user") User userForm,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        User existingUser = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        boolean isAdmin = userPrincipal.getUser().getPermissions().contains(Permission.USER_UPDATE_ALL);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("editMode", true);
            return "user/form";
        }

        existingUser.setEmail(userForm.getEmail());

        if (isAdmin) {
            existingUser.setRole(userForm.getRole());
            existingUser.setEnabled(userForm.isEnabled());
        }

        service.update(existingUser);

        return "redirect:/";
    }


    @GetMapping("/user/list")
    @PreAuthorize("hasAuthority(@permissions.USER_UPDATE_ALL())")
    public String listUsers(Model model) {
        model.addAttribute("users", service.findAll());
        return "user/list";
    }

    @GetMapping("/user/change-password")
    @PreAuthorize("hasAuthority(@permissions.USER_UPDATE())")
    public String changePasswordForm() {
        return "user/change-password";
    }

    @PostMapping("/user/change-password")
    @PreAuthorize("hasAuthority(@permissions.USER_UPDATE())")
    public String changePassword(
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "user/change-password";
        }

        if (password.length() < 8) {
            model.addAttribute("error", "Password must be at least 8 characters");
            return "user/change-password";
        }

        service.changeCurrentUserPassword(password);

        return "redirect:/";
    }
}
