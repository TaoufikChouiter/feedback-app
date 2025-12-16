package com.env.feedback.security;

public enum Permission {
    // Feedback
    FEEDBACK_CREATE,
    FEEDBACK_READ,
    FEEDBACK_READ_ALL,
    FEEDBACK_UPDATE,
    FEEDBACK_ASSIGN,

    // User management
    USER_READ,
    USER_READ_ALL,
    USER_CREATE,
    USER_UPDATE,
    USER_UPDATE_ALL,
    USER_DISABLE,

    // Dashboard / stats
    DASHBOARD_VIEW
}

