package com.lawmate.service;

import com.lawmate.dao.CategoryDAO;
import com.lawmate.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDAO dao;

    public List<CategoryDTO> categorySch(CategoryDTO sch) {
        if (sch.getName() == null) sch.setName("");
        if (sch.getDescription() == null) sch.setDescription("");

        sch.setName("%" + sch.getName() + "%");
        sch.setDescription("%" + sch.getDescription() + "%");

        return dao.categorySch(sch);
    }

    public CategoryDTO getCategoryById(int categoryId) {
        return dao.getCategoryById(categoryId);
    }

    // 추가
    public List<CategoryDTO> getCategoryViewCount() {
        return dao.getCategoryViewCount();
    }
}