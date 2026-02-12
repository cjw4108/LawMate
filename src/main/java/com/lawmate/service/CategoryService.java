package com.lawmate.service;

import com.lawmate.dao.CategoryDao;
import com.lawmate.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao dao;

    public List<CategoryDto> categorySch(CategoryDto sch) {

        // null 방어
        if (sch.getName() == null) sch.setName("");
        if (sch.getDescription() == null) sch.setDescription("");

        // LIKE 처리
        sch.setName("%" + sch.getName() + "%");
        sch.setDescription("%" + sch.getDescription() + "%");

        return dao.categorySch(sch);
    }
}