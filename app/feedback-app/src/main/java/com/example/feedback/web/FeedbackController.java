package com.example.feedback.web;

import com.example.feedback.model.Feedback;
import com.example.feedback.model.ContactType;
import com.example.feedback.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class FeedbackController {

    private final FeedbackService service;

    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String form(Model model) {
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("contactTypes", ContactType.values());
        return "form";
    }

    @PostMapping("/submit")
    public String submit(@Valid @ModelAttribute("feedback") Feedback feedback,
                         BindingResult binding,
                         Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("contactTypes", ContactType.values());
            return "form";
        }
        service.save(feedback);
        return "success";
    }
}
