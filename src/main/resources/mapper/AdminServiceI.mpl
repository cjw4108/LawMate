package com.lawmate.service;

import com.lawmate.dao.AdminDAO;
import com.lawmate.dto.AdminDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminDAO adminDAO;

    @Override
    public AdminDTO login(AdminDTO adminDTO) {
        return adminDAO.login(adminDTO);
    }
}