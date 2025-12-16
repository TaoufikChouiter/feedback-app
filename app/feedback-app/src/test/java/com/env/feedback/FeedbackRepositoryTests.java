package com.env.feedback;

import com.env.feedback.model.Feedback;
import com.env.feedback.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FeedbackRepositoryTests {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    void contextLoads() {
        // Basic context load test
    }

    @Test
    void testCreateFeedback() {
        Feedback feedback = new Feedback();
        feedback.setName("Alice");
        feedback.setEmail("alice@example.com");
        feedback.setMessage("Test feedback");
        feedback.setContactType(Feedback.ContactType.GENERAL);
        feedback.setStatus(Feedback.FeedbackStatus.OPEN);
        feedback.setPriority(Feedback.FeedbackPriority.HIGH);
        feedback.setCreatedAt(LocalDateTime.now());
        feedbackRepository.save(feedback);

        assertThat(feedback.getId()).isNotNull();
    }

    @Test
    void testFindFeedbackByStatus() {
        Feedback feedback = new Feedback();
        feedback.setName("Bob");
        feedback.setEmail("bob@example.com");
        feedback.setMessage("Another feedback");
        feedback.setContactType(Feedback.ContactType.GENERAL);
        feedback.setStatus(Feedback.FeedbackStatus.OPEN);
        feedback.setPriority(Feedback.FeedbackPriority.MEDIUM);
        feedback.setCreatedAt(LocalDateTime.now());
        feedbackRepository.save(feedback);

        List<Feedback> openFeedbacks = feedbackRepository.findAllByStatus(Feedback.FeedbackStatus.OPEN);
        assertThat(openFeedbacks).isNotEmpty();
    }

    @Test
    void testCloseFeedback() {
        Feedback feedback = new Feedback();
        feedback.setName("Charlie");
        feedback.setEmail("charlie@example.com");
        feedback.setMessage("Close me");
        feedback.setContactType(Feedback.ContactType.GENERAL);
        feedback.setStatus(Feedback.FeedbackStatus.OPEN);
        feedback.setPriority(Feedback.FeedbackPriority.LOW);
        feedback.setCreatedAt(LocalDateTime.now());
        feedbackRepository.save(feedback);

        feedback.setStatus(Feedback.FeedbackStatus.CLOSED);
        feedbackRepository.save(feedback);

        Feedback closed = feedbackRepository.findById(feedback.getId()).get();
        assertThat(closed.getStatus()).isEqualTo(Feedback.FeedbackStatus.CLOSED);
    }
}