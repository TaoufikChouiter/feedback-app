package com.env.feedback.service.impl;

import com.env.feedback.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Profile("prod")
@Service
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${custom.mail.from}")
    private String from;

    public SmtpEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendResetPassword(String to, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Reset Password");
        message.setText("Your new password is: " + password);

        mailSender.send(message);
    }
}
