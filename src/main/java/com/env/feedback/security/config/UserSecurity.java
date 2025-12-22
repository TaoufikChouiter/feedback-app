package com.env.feedback.security.config;

import com.env.feedback.model.User;
import com.env.feedback.security.permission.Permission;
import com.env.feedback.security.principal.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {
    public boolean canUpdateUser(Long userId) {
        return canUpdateUser(userId, SecurityContextHolder.getContext().getAuthentication());
    }

    public boolean canUpdateUser(User user) {
        return user != null && canUpdateUser(user.getId());
    }

    public boolean canViewUser(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        return userPrincipal.getUser().getId().equals(id)
                || userPrincipal.hasPermission(Permission.USER_READ_ALL);
    }

    private boolean canUpdateUser(Long targetUserId, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return false;
        }

        return principal.hasPermission(Permission.USER_UPDATE_ALL)
                || principal.getUser().getId().equals(targetUserId);
    }
}