package com.lawmate.service;

import com.lawmate.dao.LawContentDao;
import com.lawmate.dto.LawContentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LawContentService {

    @Autowired
    private LawContentDao dao;

    public List<LawContentDto> getContentsByCategory(int categoryId) {
        return dao.getContentsByCategory(categoryId);
    }
}