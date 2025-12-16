package com.env.feedback.service.impl;

import com.env.feedback.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class DevEmailService implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(DevEmailService.class);

    @Override
    public void sendResetPassword(String to, String password) {
        logger.info("=== DEV EMAIL ===");
        logger.info("To: {}", to);
        logger.info("Subject: Reset Password");
        logger.info("Password: {}", password);
        logger.info("================");
    }
}

