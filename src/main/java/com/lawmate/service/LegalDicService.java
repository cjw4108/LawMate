package com.lawmate.service;

import com.lawmate.dao.LegalDicDAO;
import com.lawmate.dto.LegalDicDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegalDicService {

    private final LegalDicDAO legalDicDAO;

    public LegalDicService(LegalDicDAO legalDicDAO) {
        this.legalDicDAO = legalDicDAO;
    }

    public List<LegalDicDTO> getAll() {
        return legalDicDAO.selectAll();
    }

    public List<LegalDicDTO> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return legalDicDAO.selectAll();
        }
        return legalDicDAO.selectByKeyword(keyword.trim());
    }

    public List<LegalDicDTO> getByChosung(String chosung) {
        if (chosung == null || chosung.trim().isEmpty()) {
            return legalDicDAO.selectAll();
        }
        return legalDicDAO.selectByChosung(chosung.trim());
    }

    public LegalDicDTO getById(Long id) {
        return legalDicDAO.selectById(id);
    }
}