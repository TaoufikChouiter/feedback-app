package com.env.feedback.security.config;

import com.env.feedback.repository.FeedbackRepository;
import com.env.feedback.security.principal.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeedbackSecurity {
    private final FeedbackRepository repo;

    public FeedbackSecurity(FeedbackRepository repo) {
        this.repo = repo;
    }

    public boolean isOwner(Long feedbackId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) auth.getPrincipal();
        return repo.isOwner(feedbackId, user.getUser().getId());
    }

    public boolean isAssignedTo(Long feedbackId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) auth.getPrincipal();
        return repo.isAssignedTo(feedbackId, user.getUser().getId());
    }
}
