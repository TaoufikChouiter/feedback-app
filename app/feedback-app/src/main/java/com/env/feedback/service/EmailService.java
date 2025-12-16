package com.env.feedback.service;

public interface EmailService {
    void sendResetPassword(String to, String password);
}

