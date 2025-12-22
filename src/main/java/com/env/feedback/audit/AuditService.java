package com.env.feedback.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT_LOG");

    public void log(String username, String action, Object details) {
        String message = String.format("[%s] User: %s | Action: %s | Details: %s",
                LocalDateTime.now(), username, action, details);
        auditLogger.info(message);
    }
}