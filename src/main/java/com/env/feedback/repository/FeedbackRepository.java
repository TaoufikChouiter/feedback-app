package com.env.feedback.repository;

import com.env.feedback.dto.FeedbackSearchCriteria;
import com.env.feedback.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>, JpaSpecificationExecutor<Feedback> {
    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM Feedback f WHERE f.id = :feedbackId AND f.createdBy.id = :userId) THEN true ELSE false END")
    boolean isOwner(@Param("feedbackId") Long feedbackId, @Param("userId") Long userId);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM Feedback f WHERE f.id = :feedbackId AND f.assignedTo.id = :userId) THEN true ELSE false END")
    boolean isAssignedTo(@Param("feedbackId") Long feedbackId, @Param("userId") Long userId);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    long countByStatus(Feedback.FeedbackStatus status);

    long countByStatusAndCreatedAtBetween(Feedback.FeedbackStatus status, LocalDateTime start, LocalDateTime end);

    @Query("SELECT f.contactType AS name, COUNT(f) AS count FROM Feedback f GROUP BY f.contactType ORDER BY COUNT(f) DESC")
    List<Object[]> findTopCategories();

    List<Feedback> findAllByStatus(Feedback.FeedbackStatus feedbackStatus);
}