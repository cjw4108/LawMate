package com.lawmate.service;

import com.lawmate.dao.ConsultDao;
import com.lawmate.dto.ConsultDto;
import com.lawmate.dto.ConsultSchDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultService {
    @Autowired(required = false)
    private ConsultDao dao;

    public List<ConsultDto> getConsultList(@NotNull ConsultSchDto sch) {
        if (sch.getSchTitle() == null) {
            sch.setSchTitle("");
        }
        sch.setSchTitle("%" + sch.getSchTitle() + "%");
        return dao.getConsultList(sch);
    }

    public String consultInsert(ConsultDto ins) {
        return dao.insertConsult(ins) > 0 ? "등록성공" : "등록되지 않았습니다";
    }

    public ConsultDto getConsult(int id) {
        return dao.getConsult(id);
    }

    // ====== 마이페이지 추가 메서드 ======

    // 1. 일반회원 상담 내역 조회 (UserPageController 연동)
    public List<ConsultDto> getUserConsultList(String userId) {
        return dao.getConsultListByUserId(userId);
    }

    // 2. 변호사 담당 상담 목록 조회 (LawyerMypageController 연동)
    public List<ConsultDto> getLawyerConsultList(String lawyerId) {
        return dao.getConsultListByLawyerId(lawyerId);
    }

    // 3. 상담 상태 업데이트 (승인/완료 처리용)
    @Transactional // 데이터 변경 작업이므로 추가하면 더 안전합니다.
    public void updateConsultStatus(int id, String status) {
        dao.updateConsultStatus(id, status);
    }

    // 4. 마이페이지 대시보드용 건수 조회 (추가 권장)
    public int getConsultCountByUserId(String userId) {
        return dao.getConsultCountByUserId(userId);
    }

    public Object getConsultListByUserId(String userId) {
        return null;
    }
}