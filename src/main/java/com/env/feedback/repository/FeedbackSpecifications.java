package com.env.feedback.repository;

import com.env.feedback.model.Feedback;
import com.env.feedback.model.User;
import com.env.feedback.security.permission.Permission;
import com.env.feedback.security.principal.UserPrincipal;
import org.springframework.data.jpa.domain.Specification;
import java.time.OffsetDateTime;

public class FeedbackSpecifications {
    public static Specification<Feedback> ownerOrAssigned(UserPrincipal user) {
        if(user.getUser().getRole().getPermissions().contains(Permission.FEEDBACK_READ_ALL)) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> cb.or(
                cb.equal(root.get("createdBy").get("id"), user.getUser().getId()),
                cb.equal(root.get("assignedTo").get("id"), user.getUser().getId())
        );
    }

    public static Specification<Feedback> hasContactType(Feedback.ContactType contactType) {
        return (root, query, cb) -> contactType == null ? cb.conjunction() : cb.equal(root.get("contactType"), contactType);
    }

    public static Specification<Feedback> hasStatus(Feedback.FeedbackStatus status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Feedback> hasPriority(Feedback.FeedbackPriority priority) {
        return (root, query, cb) -> priority == null ? cb.conjunction() : cb.equal(root.get("priority"), priority);
    }

    public static Specification<Feedback> messageContains(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("message")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Feedback> assignedTo(User assignedTo) {
        return (root, query, cb) -> {
            if (assignedTo == null || assignedTo.getId() == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("assignedTo").get("id"), assignedTo.getId());
        };
    }

    public static Specification<Feedback> submittedAfter(OffsetDateTime dateFrom) {
        return (root, query, cb) -> dateFrom == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("createdAt"), dateFrom);
    }

    public static Specification<Feedback> submittedBefore(OffsetDateTime dateTo) {
        return (root, query, cb) -> dateTo == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("createdAt"), dateTo);
    }
}

