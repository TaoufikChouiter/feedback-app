package com.env.feedback.controller;

import com.env.feedback.model.Feedback;
import com.env.feedback.security.permission.Permission;
import com.env.feedback.security.principal.UserPrincipal;
import com.env.feedback.service.FeedbackService;
import com.env.feedback.service.UserService;
import com.env.feedback.web.controller.FeedbackController;
import com.env.feedback.web.dto.FeedbackSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FeedbackController feedbackController;

    private MockMvc mockMvc;
    private UserPrincipal mockUserPrincipal;
    private Feedback feedback;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(feedbackController).build();

        feedback = new Feedback();
        feedback.setId(1L);
        feedback.setNote("Test note");

        mockUserPrincipal = mock(UserPrincipal.class);
        when(mockUserPrincipal.hasPermission(Permission.FEEDBACK_READ)).thenReturn(true);
        when(mockUserPrincipal.hasPermission(Permission.FEEDBACK_UPDATE)).thenReturn(true);
    }

    @Test
    void getFeedbackForm_shouldReturnForm() {
        Model model = mock(Model.class);
        String view = feedbackController.create(model);
        assertEquals("feedback/form", view);
        verify(model).addAttribute(eq("feedback"), any(Feedback.class));
        verify(model).addAttribute(eq("contactTypes"), any());
    }

    @Test
    void createFeedback_valid_shouldRedirect() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        Feedback newFeedback = new Feedback();
        String view = feedbackController.create(newFeedback, bindingResult, mock(Model.class), mockUserPrincipal);

        verify(feedbackService).create(newFeedback);
        assertEquals("redirect:/feedback/list", view);
    }

    @Test
    void createFeedback_withErrors_shouldReturnForm() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        Feedback newFeedback = new Feedback();
        Model model = mock(Model.class);

        String view = feedbackController.create(newFeedback, bindingResult, model, mockUserPrincipal);
        assertEquals("feedback/form", view);
        verify(model).addAttribute(eq("contactTypes"), any());
        verify(feedbackService, never()).create(any());
    }

    @Test
    void getFeedbackById_shouldReturnForm() {
        Model model = mock(Model.class);
        when(feedbackService.getFeedback(1L)).thenReturn(Optional.of(feedback));

        String view = feedbackController.get(model, 1L);

        assertEquals("feedback/form", view);
        verify(model).addAttribute("feedback", feedback);
        verify(model).addAttribute("contactTypes", Feedback.ContactType.values());
    }

    @Test
    void manageFeedback_shouldReturnManageForm() {
        when(feedbackService.getFeedback(1L)).thenReturn(Optional.of(feedback));
        when(userService.findAssignableUsers()).thenReturn(List.of());

        Model model = mock(Model.class);
        String view = feedbackController.manageFeedback(1L, model);

        assertEquals("feedback/manage", view);
        verify(model).addAttribute("feedback", feedback);
        verify(model).addAttribute("contactTypes", Feedback.ContactType.values());
        verify(model).addAttribute("statusOptions", Feedback.FeedbackStatus.values());
        verify(model).addAttribute("priorityOptions", Feedback.FeedbackPriority.values());
        verify(model).addAttribute("assignableUsers", List.of());
    }

    @Test
    void updateFeedback_shouldCallServiceAndRedirect() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(feedbackService.getFeedback(1L)).thenReturn(Optional.of(feedback));
        when(userService.findAssignableUsers()).thenReturn(List.of());

        Feedback form = new Feedback();
        form.setNote("Updated note");
        form.setStatus(Feedback.FeedbackStatus.OPEN);
        form.setPriority(Feedback.FeedbackPriority.HIGH);

        Model model = mock(Model.class);
        String view = feedbackController.updateFeedback(1L, form, bindingResult, model);

        verify(feedbackService).update(feedback);
        assertEquals("redirect:/feedback/list", view);
        assertEquals("Updated note", feedback.getNote());
        assertEquals(Feedback.FeedbackStatus.OPEN, feedback.getStatus());
        assertEquals(Feedback.FeedbackPriority.HIGH, feedback.getPriority());
    }

    @Test
    void listFeedback_shouldAddAttributesAndReturnListView() {
        FeedbackSearchCriteria criteria = new FeedbackSearchCriteria();
        criteria.setPage(0);
        criteria.setSize(10);

        Page<Feedback> page = new PageImpl<>(List.of(feedback));
        when(feedbackService.searchFeedback(eq(criteria), any(Pageable.class))).thenReturn(page);
        when(userService.findAssignableUsers()).thenReturn(List.of());

        Model model = mock(Model.class);
        String view = feedbackController.listFeedback(criteria, model, mockUserPrincipal);

        assertEquals("feedback/list", view);
        verify(model).addAttribute("canManage", true);
        verify(model).addAttribute("feedbackPage", page);
        verify(model).addAttribute("criteria", criteria);
        verify(model).addAttribute("totalPages", page.getTotalPages());
        verify(model).addAttribute("currentPage", criteria.getPage());
        verify(model).addAttribute("contactTypes", Feedback.ContactType.values());
        verify(model).addAttribute("statuses", Feedback.FeedbackStatus.values());
        verify(model).addAttribute("priorities", Feedback.FeedbackPriority.values());
    }

    @Test
    void thanks_shouldReturnView() {
        String view = feedbackController.thanks();
        assertEquals("feedback/thanks", view);
    }
}