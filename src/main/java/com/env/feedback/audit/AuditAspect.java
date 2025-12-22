package com.env.feedback.audit;

import com.env.feedback.security.principal.UserPrincipal;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @AfterReturning("@annotation(com.env.feedback.audit.Auditable)")
    public void auditMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Auditable annotation = signature.getMethod().getAnnotation(Auditable.class);

        String action = annotation.action().isEmpty()
                ? signature.getMethod().getName()
                : annotation.action();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "anonymous";
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal userPrincipal) {
            username = userPrincipal.getUsername();
        }

        String details = buildAuditDetails(joinPoint.getArgs());

        auditService.log(username, action, details);
    }

    private String buildAuditDetails(Object[] args) {
        if (args == null || args.length == 0) {
            return "-";
        }

        return Arrays.stream(args)
                .filter(Objects::nonNull)
                .map(arg -> {
                    if (arg instanceof AuditLoggable auditLoggable) {
                        return auditLoggable.toAuditString();
                    }
                    return arg.toString();
                })
                .collect(Collectors.joining(", ", "[", "]"));
    }
}