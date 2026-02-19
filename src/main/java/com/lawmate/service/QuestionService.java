package com.lawmate.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.lawmate.dto.Question;
import com.lawmate.dao.QuestionRepository;

@Service
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;

    // ✅ Lombok 제거
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not Found"));
    }

    public void delete(Long id) {
        questionRepository.deleteById(id);
    }
}
