package com.example.feedback.service;

import com.example.feedback.model.Feedback;
import com.example.feedback.model.ContactType;
import com.example.feedback.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class FeedbackService {
    private final FeedbackRepository repo;

    public FeedbackService(FeedbackRepository repo) {
        this.repo = repo;
    }

    public Feedback save(Feedback f) {
        return repo.save(f);
    }

    public List<Feedback> listAll() {
        return repo.findAll();
    }

    public List<Feedback> listByType(ContactType type) {
        return repo.findByContactType(type);
    }
}
