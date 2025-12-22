package com.env.feedback.security.permission;

import org.springframework.stereotype.Component;

@Component("permissions")
public class PermissionExpressions {
    // ---------- Feedbacks ----------
    public String FEEDBACK_CREATE() {
        return Permission.FEEDBACK_CREATE.name();
    }

    public String FEEDBACK_READ() {
        return Permission.FEEDBACK_READ.name();
    }

    public String FEEDBACK_READ_ALL() {
        return Permission.FEEDBACK_READ_ALL.name();
    }

    public String FEEDBACK_UPDATE() {
        return Permission.FEEDBACK_UPDATE.name();
    }

    public String FEEDBACK_ASSIGN() {
        return Permission.FEEDBACK_ASSIGN.name();
    }

    // ---------- User management ----------
    public String USER_READ() {
        return Permission.USER_READ.name();
    }

    public String USER_READ_ALL() {
        return Permission.USER_READ_ALL.name();
    }

    public String USER_CREATE() {
        return Permission.USER_CREATE.name();
    }

    public String USER_UPDATE() {
        return Permission.USER_UPDATE.name();
    }

    public String USER_UPDATE_ALL() {
        return Permission.USER_UPDATE_ALL.name();
    }

    public String USER_DISABLE() {
        return Permission.USER_DISABLE.name();
    }

    // ---------- Dashboard / stats ----------
    public String DASHBOARD_VIEW() {
        return Permission.DASHBOARD_VIEW.name();
    }
}