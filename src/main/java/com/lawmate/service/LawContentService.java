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

    // 법률정보 상세 조회
    public LawContentDto getContentById(int contentId) {
        return dao.getContentById(contentId);
    }

    // 조회수 증가
    public void increaseViewCount(int contentId) {
        dao.increaseViewCount(contentId);
    }
}