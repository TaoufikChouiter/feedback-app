package com.env.feedback.model;

import com.env.feedback.audit.AuditLoggable;
import com.env.feedback.security.permission.Permission;
import com.env.feedback.security.permission.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity
@Table(name = "app_user")
public class User implements AuditLoggable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(max=100)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password; //Encrypted (BCrypt)

    @Column(unique = true, nullable = false)
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private boolean passwordChangeRequired = true;

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Permission> getPermissions() {
        return getRole().getPermissions();
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toAuditString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", role=" + role +
                '}';
    }


    public static final class Builder {

        private String username;
        private String password;
        private String email;
        private Role role;
        private boolean enabled = true;
        private boolean passwordChangeRequired = false;

        private Builder() {
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder passwordChangeRequired(boolean passwordChangeRequired) {
            this.passwordChangeRequired = passwordChangeRequired;
            return this;
        }

        public User build() {
            User user = new User();
            user.username = this.username;
            user.password = this.password;
            user.email = this.email;
            user.role = this.role;
            user.enabled = this.enabled;
            user.passwordChangeRequired = this.passwordChangeRequired;
            return user;
        }
    }

}
