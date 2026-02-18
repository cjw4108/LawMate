package com.lawmate.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lawmate.dto.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
