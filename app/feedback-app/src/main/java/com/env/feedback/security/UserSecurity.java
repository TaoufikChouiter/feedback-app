package com.env.feedback.security;

import com.env.feedback.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {
    public boolean canUpdateUser(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        return userPrincipal.getUser().getId().equals(user.getId())
                || userPrincipal.hasPermission(Permission.USER_UPDATE_ALL);
    }

    public boolean canViewUser(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        return userPrincipal.getUser().getId().equals(id)
                || userPrincipal.hasPermission(Permission.USER_READ_ALL);
    }
}