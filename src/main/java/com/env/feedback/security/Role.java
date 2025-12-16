package com.env.feedback.security;

import java.util.Set;

import static com.env.feedback.security.Permission.*;

public enum Role {
    ADMIN(Set.of(
            FEEDBACK_CREATE,
            FEEDBACK_READ,
            FEEDBACK_READ_ALL,
            FEEDBACK_UPDATE,
            FEEDBACK_ASSIGN,
            USER_READ,
            USER_READ_ALL,
            USER_CREATE,
            USER_UPDATE,
            USER_UPDATE_ALL,
            USER_DISABLE,
            DASHBOARD_VIEW
    )),

    FEEDBACK_MANAGER(Set.of(
            FEEDBACK_CREATE,
            FEEDBACK_READ,
            FEEDBACK_READ_ALL,
            FEEDBACK_UPDATE,
            FEEDBACK_ASSIGN,
            DASHBOARD_VIEW,
            USER_READ,
            USER_UPDATE
    )),

    USER(Set.of(
            FEEDBACK_CREATE,
            FEEDBACK_READ,
            USER_READ,
            USER_UPDATE
    )),

    READ_ONLY(Set.of(
            FEEDBACK_READ_ALL,
            DASHBOARD_VIEW,
            USER_READ,
            USER_READ_ALL,
            USER_UPDATE
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
