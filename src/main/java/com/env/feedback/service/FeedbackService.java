package com.env.feedback.service;

import com.env.feedback.dto.FeedbackSearchCriteria;
import com.env.feedback.model.Feedback;
import com.env.feedback.repository.FeedbackRepository;
import com.env.feedback.repository.FeedbackSpecifications;
import com.env.feedback.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Objects;

@Service
@Transactional
public class FeedbackService {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);

    private final FeedbackRepository repo;

    public FeedbackService(FeedbackRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasAuthority(@permissions.FEEDBACK_READ_ALL()) or @feedbackSecurity.isAssignedTo(#id) or @feedbackSecurity.isOwner(#id)")
    public Feedback getFeedback(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void create(Feedback feedback) {
        logger.info("Creating feedback of type : {}", feedback.getContactType());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.getPrincipal() instanceof UserPrincipal userPrincipal){
            feedback.setCreatedBy(userPrincipal.getUser());
        }

        feedback.setCreatedAt(LocalDateTime.now());
        repo.save(feedback);
    }

    @PreAuthorize("hasAuthority(@permissions.FEEDBACK_UPDATE())")
    public void update(Feedback feedback) {
        logger.info("Updating feedback with status : {}", feedback.getStatus());
        if(feedback.getStatus().equals(Feedback.FeedbackStatus.CLOSED)){
            feedback.setClosedAt(LocalDateTime.now());
        }

        repo.save(feedback);
    }

    @PreAuthorize("hasAuthority(@permissions.FEEDBACK_READ_ALL()) or hasAuthority(@permissions.FEEDBACK_READ())")
    public Page<Feedback> searchFeedback(FeedbackSearchCriteria criteria, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) auth.getPrincipal();

        Specification<Feedback> spec = FeedbackSpecifications.ownerOrAssigned(user)
                .and(FeedbackSpecifications.hasContactType(criteria.getContactType()))
                .and(FeedbackSpecifications.hasStatus(criteria.getStatus()))
                .and(FeedbackSpecifications.hasPriority(criteria.getPriority()))
                .and(FeedbackSpecifications.messageContains(criteria.getMessage()))
                .and(FeedbackSpecifications.assignedTo(criteria.getAssignedTo()))
                .and(FeedbackSpecifications.submittedAfter(criteria.getDateFrom() != null ? criteria.getDateFrom().atStartOfDay().atOffset(OffsetDateTime.now().getOffset()) : null))
                .and(FeedbackSpecifications.submittedBefore(criteria.getDateTo() != null ? criteria.getDateTo().atTime(23,59,59).atOffset(OffsetDateTime.now().getOffset()) : null));

        return repo.findAll(spec, pageable);
    }

    @PreAuthorize("hasAuthority(@permissions.FEEDBACK_READ_ALL()) or hasAuthority(@permissions.FEEDBACK_READ())")
    public long getTotalFeedbacks(UserPrincipal user) {
        Specification<Feedback> spec = FeedbackSpecifications.ownerOrAssigned(user);

        return repo.count(spec);
    }
}
