package com.env.feedback.web.dto;

import com.env.feedback.security.password.PasswordMatches;
import com.env.feedback.security.password.StrongPassword;
import jakarta.validation.constraints.NotBlank;

@PasswordMatches
public class ChangePasswordDto {

    @NotBlank
    private String oldPassword;

    @StrongPassword
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;

    public ChangePasswordDto() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}

