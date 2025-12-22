package com.env.feedback.model;

import com.env.feedback.audit.AuditLoggable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback implements AuditLoggable {

    public enum FeedbackStatus { OPEN, IN_PROGRESS, CLOSED }
    public enum FeedbackPriority { LOW, MEDIUM, HIGH, CRITICAL }
    public enum ContactType { GENERAL, MARKETING, SUPPORT }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max=100)
    private String name;

    @Size(max=100)
    @Email
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @NotNull
    @Size(max=1000)
    @Column(length=1000)
    private String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FeedbackStatus status = FeedbackStatus.OPEN;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FeedbackPriority priority = FeedbackPriority.MEDIUM;

    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    @Size(max=1000)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public ContactType getContactType() { return contactType; }
    public void setContactType(ContactType contactType) { this.contactType = contactType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toAuditString() {
        return "Feedback{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contactType=" + contactType +
                ", status=" + status +
                ", priority=" + priority +
                ", note='" + note + '\'' +
                ", assignedTo=" + assignedTo +
                ", message='" + message + '\'' +
                '}';
    }

    public FeedbackStatus getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }

    public FeedbackPriority getPriority() {
        return priority;
    }

    public void setPriority(FeedbackPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }


    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}