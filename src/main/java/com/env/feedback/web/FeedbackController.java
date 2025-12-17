package com.env.feedback.web;

import com.env.feedback.dto.FeedbackSearchCriteria;
import com.env.feedback.model.Feedback;
import com.env.feedback.model.User;
import com.env.feedback.security.Permission;
import com.env.feedback.security.UserPrincipal;
import com.env.feedback.service.FeedbackService;
import com.env.feedback.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FeedbackController {
    private final FeedbackService service;
    private final UserService userService;

    public FeedbackController(FeedbackService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping("/feedback")
    public String create(Model model) {
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("contactTypes", Feedback.ContactType.values());
        return "feedback/form";
    }

    @PostMapping("/feedback")
    public String create(@Valid @ModelAttribute("feedback") Feedback feedback,
                         BindingResult binding,
                         Model model,
                         @AuthenticationPrincipal UserPrincipal user) {
        if (binding.hasErrors()) {
            model.addAttribute("contactTypes", Feedback.ContactType.values());
            return "feedback/form";
        }
        service.create(feedback);

        if(user != null && user.hasPermission(Permission.FEEDBACK_READ)){
            return "redirect:/feedback/list";
        }else{
            return "redirect:/feedback/thanks";
        }
    }


    @GetMapping("/feedback/{id}")
    @PreAuthorize("hasAuthority(@permissions.FEEDBACK_READ_ALL()) or @feedbackSecurity.isAssignedTo(#id) or @feedbackSecurity.isOwner(#id)")
    public String get(Model model, @PathVariable Long id) {
        Feedback feedback = service.getFeedback(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("feedback", feedback);
        model.addAttribute("contactTypes", Feedback.ContactType.values());

        return "feedback/form";
    }

    @GetMapping("/feedback/{id}/manage")
    @PreAuthorize("hasAuthority(@permissions.FEEDBACK_READ_ALL()) or @feedbackSecurity.isAssignedTo(#id) or @feedbackSecurity.isOwner(#id)")
    public String manageFeedback(@PathVariable Long id, Model model) {
        Feedback feedback = service.getFeedback(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("feedback", feedback);
        model.addAttribute("contactTypes", Feedback.ContactType.values());
        model.addAttribute("statusOptions", Feedback.FeedbackStatus.values());
        model.addAttribute("priorityOptions", Feedback.FeedbackPriority.values());
        model.addAttribute("assignableUsers", userService.findAssignableUsers());

        return "feedback/manage";
    }

    @PostMapping("/feedback/{id}/update")
    @PreAuthorize("@feedbackSecurity.isAssignedTo(#id) or @feedbackSecurity.isOwner(#id) or hasAuthority(@permissions.FEEDBACK_UPDATE())")
    public String updateFeedback(
            @PathVariable Long id,
            @ModelAttribute("feedback") Feedback feedbackForm,
            BindingResult bindingResult,
            Model model) {

        Feedback feedback = service.getFeedback(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        feedback.setStatus(feedbackForm.getStatus());
        feedback.setPriority(feedbackForm.getPriority());
        feedback.setAssignedTo(feedbackForm.getAssignedTo());
        feedback.setNote(feedbackForm.getNote());

        if (bindingResult.hasErrors()) {
            model.addAttribute("statusOptions", Feedback.FeedbackStatus.values());
            model.addAttribute("priorityOptions", Feedback.FeedbackPriority.values());
            model.addAttribute("assignableUsers", userService.findAssignableUsers());
            return "feedback/manage";
        }

        service.update(feedback);

        return "redirect:/feedback/list"; // or wherever you want
    }

    @GetMapping("/feedback/list")
    @PreAuthorize("hasAuthority(@permissions.FEEDBACK_READ_ALL()) or hasAuthority(@permissions.FEEDBACK_READ())")
    public String listFeedback(
            FeedbackSearchCriteria criteria,
            Model model,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize());
        Page<Feedback> feedbackPage = service.searchFeedback(criteria, pageable);

        model.addAttribute("canManage", true);
        model.addAttribute("feedbackPage", feedbackPage);
        model.addAttribute("criteria", criteria);
        model.addAttribute("totalPages", feedbackPage.getTotalPages());
        model.addAttribute("currentPage", criteria.getPage());
        model.addAttribute("contactTypes", Feedback.ContactType.values());
        model.addAttribute("statuses", Feedback.FeedbackStatus.values());
        model.addAttribute("priorities", Feedback.FeedbackPriority.values());
        if(userPrincipal.hasPermission(Permission.FEEDBACK_UPDATE)){
            model.addAttribute("assignableUsers", userService.findAssignableUsers());
        }

        return "feedback/list";
    }

    @GetMapping("/feedback/thanks")
    public String thanks() {
        return "feedback/thanks";
    }
}
