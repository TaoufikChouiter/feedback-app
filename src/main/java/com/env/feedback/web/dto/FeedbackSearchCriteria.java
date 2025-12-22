package com.env.feedback.web.dto;

import com.env.feedback.model.Feedback;
import com.env.feedback.model.User;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class FeedbackSearchCriteria {
    private String message;
    private Feedback.ContactType contactType;
    private Feedback.FeedbackStatus status;
    private Feedback.FeedbackPriority priority;
    private User assignedTo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTo;

    private int page = 0;
    private int size = 10;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Feedback.ContactType getContactType() { return contactType; }
    public void setContactType(Feedback.ContactType contactType) { this.contactType = contactType; }

    public Feedback.FeedbackStatus getStatus() { return status; }
    public void setStatus(Feedback.FeedbackStatus status) { this.status = status; }

    public Feedback.FeedbackPriority getPriority() { return priority; }
    public void setPriority(Feedback.FeedbackPriority priority) { this.priority = priority; }

    public LocalDate getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDate dateFrom) { this.dateFrom = dateFrom; }

    public LocalDate getDateTo() { return dateTo; }
    public void setDateTo(LocalDate dateTo) { this.dateTo = dateTo; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }
}

