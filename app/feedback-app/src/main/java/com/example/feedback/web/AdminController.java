package com.example.feedback.web;

import com.example.feedback.model.Feedback;
import com.example.feedback.model.ContactType;
import com.example.feedback.service.FeedbackService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final FeedbackService service;

    public AdminController(FeedbackService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public String list(@RequestParam(value="type", required=false) ContactType type,
                       @RequestParam(value="sort", defaultValue="desc") String sort,
                       Model model) {
        List<Feedback> list;
        if (type != null) {
            list = service.listByType(type);
        } else {
            list = service.listAll();
        }
        // sort by submittedAt
        list.sort((a,b) -> {
            int m = sort.equalsIgnoreCase("asc") ? 1 : -1;
            return m * a.getSubmittedAt().compareTo(b.getSubmittedAt());
        });
        model.addAttribute("feedbacks", list);
        model.addAttribute("filterType", type);
        model.addAttribute("sortOrder", sort);
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX"));
        return "admin/list";
    }
}
