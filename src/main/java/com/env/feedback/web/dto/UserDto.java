package com.env.feedback.web.dto;

import com.env.feedback.security.permission.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto {

    @Size(max = 100, message = "Username cannot exceed 100 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotNull
    private Role role = Role.USER;

    @NotNull
    private Boolean enabled = true;

    public UserDto() {
    }

    public UserDto(String username, String email, Role role, Boolean enabled) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

