package com.env.feedback.web;

import com.env.feedback.security.Permission;
import com.env.feedback.security.UserPrincipal;
import com.env.feedback.service.FeedbackMetricsService;
import com.env.feedback.service.FeedbackService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final FeedbackService feedbackService;
    private final FeedbackMetricsService feedbackMetricsService;

    public HomeController(FeedbackService feedbackService, FeedbackMetricsService feedbackMetricsService) {
        this.feedbackService = feedbackService;
        this.feedbackMetricsService = feedbackMetricsService;
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserPrincipal user) {
        if (user != null && user.hasPermission(Permission.DASHBOARD_VIEW)) {
            model.addAttribute("totalFeedbacks", feedbackService.getTotalFeedbacks(user));
            model.addAttribute("monthlyFeedbacks", feedbackMetricsService.countThisMonth());
            model.addAttribute("monthlyFeedbacksChange", feedbackMetricsService.monthlyChangePercentage());
            model.addAttribute("openFeedbacks", feedbackMetricsService.countOpen());
            model.addAttribute("closedFeedbacks", feedbackMetricsService.countClosedThisMonth());
            model.addAttribute("closedFeedbacksChange", feedbackMetricsService.closedMonthlyChangePercentage());
            model.addAttribute("percentageClosed", feedbackMetricsService.percentageClosed());
            model.addAttribute("avgResolutionTime", feedbackMetricsService.avgResolutionTimeDays());
            model.addAttribute("topCategories", feedbackMetricsService.topCategories());
        }

        return "home";
    }
}
