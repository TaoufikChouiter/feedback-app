package com.env.feedback.service;

import com.env.feedback.model.Feedback;
import com.env.feedback.model.Feedback.FeedbackStatus;
import com.env.feedback.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeedbackMetricsService {

    private final FeedbackRepository repository;

    public FeedbackMetricsService(FeedbackRepository repository) {
        this.repository = repository;
    }

    public long countAll() {
        return repository.count();
    }

    public long countThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        return repository.countByCreatedAtBetween(start, end);
    }

    public long countOpen() {
        return repository.countByStatus(FeedbackStatus.OPEN);
    }

    public long countClosedThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        return repository.countByStatusAndCreatedAtBetween(FeedbackStatus.CLOSED, start, end);
    }

    public double percentageClosed() {
        long total = countAll();
        long closed = repository.countByStatus(FeedbackStatus.CLOSED);
        return total == 0 ? 0 : ((double) closed / total) * 100;
    }

    public double avgResolutionTimeDays() {
        List<Feedback> closed = repository.findAllByStatus(FeedbackStatus.CLOSED);
        if (closed.isEmpty()) return 0;

        return closed.stream()
                .filter(f -> f.getClosedAt() != null)
                .mapToDouble(f -> Duration.between(f.getCreatedAt(), f.getClosedAt()).toDays())
                .average()
                .orElse(0);
    }

    public List<Map<String, Object>> topCategories() {
        return repository.findTopCategories().stream()
                .map(obj -> Map.of("name", obj[0], "count", obj[1]))
                .collect(Collectors.toList());
    }

    public String monthlyChangePercentage() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thisMonthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime lastMonthStart = thisMonthStart.minusMonths(1);
        LocalDateTime lastMonthEnd = thisMonthStart.minusNanos(1);

        long thisMonth = repository.countByCreatedAtBetween(thisMonthStart, now);
        long lastMonth = repository.countByCreatedAtBetween(lastMonthStart, lastMonthEnd);

        if (lastMonth == 0) return "N/A";
        double change = ((double) (thisMonth - lastMonth) / lastMonth) * 100;
        return String.format("%+.0f%%", change);
    }

    public String closedMonthlyChangePercentage() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thisMonthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime lastMonthStart = thisMonthStart.minusMonths(1);
        LocalDateTime lastMonthEnd = thisMonthStart.minusNanos(1);

        long thisMonthClosed = repository.countByStatusAndCreatedAtBetween(FeedbackStatus.CLOSED, thisMonthStart, now);
        long lastMonthClosed = repository.countByStatusAndCreatedAtBetween(FeedbackStatus.CLOSED, lastMonthStart, lastMonthEnd);

        if (lastMonthClosed == 0) return "N/A";
        double change = ((double) (thisMonthClosed - lastMonthClosed) / lastMonthClosed) * 100;
        return String.format("%+.0f%%", change);
    }
}
