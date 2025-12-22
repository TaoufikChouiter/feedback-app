package com.env.feedback.service;

import com.env.feedback.model.Feedback;
import com.env.feedback.model.User;
import com.env.feedback.repository.FeedbackRepository;
import com.env.feedback.web.dto.FeedbackSearchCriteria;
import com.env.feedback.security.principal.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository repo;

    private User testUser;
    private UserPrincipal testPrincipal;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user1");

        testPrincipal = mock(UserPrincipal.class);
        lenient().when(testPrincipal.getUser()).thenReturn(testUser);

        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(testPrincipal, null, "ROLE_FEEDBACK_MANAGER");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // ============================
    // create
    // ============================
    @Test
    void create_shouldSetCreatedByAndCreatedAtAndSave() {
        Feedback feedback = new Feedback();
        feedback.setContactType(Feedback.ContactType.GENERAL);

        feedbackService.create(feedback);

        assertNotNull(feedback.getCreatedAt());
        assertEquals(testUser, feedback.getCreatedBy());
        verify(repo).save(feedback);
    }

    @Test
    void create_noAuth_shouldStillSetCreatedAt() {
        SecurityContextHolder.getContext().setAuthentication(null);

        Feedback feedback = new Feedback();
        feedback.setContactType(Feedback.ContactType.SUPPORT);

        feedbackService.create(feedback);

        assertNotNull(feedback.getCreatedAt());
        assertNull(feedback.getCreatedBy());
        verify(repo).save(feedback);
    }

    // ============================
    // update
    // ============================
    @Test
    void update_openFeedback_shouldSaveWithoutClosedAt() {
        Feedback feedback = new Feedback();
        feedback.setStatus(Feedback.FeedbackStatus.OPEN);

        feedbackService.update(feedback);

        assertNull(feedback.getClosedAt());
        verify(repo).save(feedback);
    }

    @Test
    void update_closedFeedback_shouldSetClosedAt() {
        Feedback feedback = new Feedback();
        feedback.setStatus(Feedback.FeedbackStatus.CLOSED);

        feedbackService.update(feedback);

        assertNotNull(feedback.getClosedAt());
        verify(repo).save(feedback);
    }

    // ============================
    // getFeedback
    // ============================
    @Test
    void getFeedback_existingId_shouldReturnFeedback() {
        Feedback feedback = new Feedback();
        when(repo.findById(1L)).thenReturn(Optional.of(feedback));

        Optional<Feedback> result = feedbackService.getFeedback(1L);

        assertTrue(result.isPresent());
        assertEquals(feedback, result.get());
    }

    @Test
    void getFeedback_nonExistingId_shouldReturnEmpty() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Optional<Feedback> result = feedbackService.getFeedback(99L);

        assertTrue(result.isEmpty());
    }

    // ============================
    // searchFeedback
    // ============================
    @Test
    void searchFeedback_shouldCallRepositoryWithSpecification() {
        FeedbackSearchCriteria criteria = new FeedbackSearchCriteria();
        criteria.setContactType(Feedback.ContactType.GENERAL);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Feedback> page = new PageImpl<>(List.of(new Feedback()));
        when(((org.springframework.data.jpa.repository.JpaSpecificationExecutor<Feedback>) repo)
                .findAll(any(), eq(pageable))).thenReturn(page);

        Page<Feedback> result = feedbackService.searchFeedback(criteria, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(((org.springframework.data.jpa.repository.JpaSpecificationExecutor<Feedback>) repo)).findAll(any(), eq(pageable));
    }

    @Test
    void searchFeedback_noAuth_shouldThrowAccessDenied() {
        SecurityContextHolder.getContext().setAuthentication(null);
        FeedbackSearchCriteria criteria = new FeedbackSearchCriteria();
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(AccessDeniedException.class, () -> feedbackService.searchFeedback(criteria, pageable));
    }
}
