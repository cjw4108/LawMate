package com.lawmate.service;

import com.lawmate.dao.ConsultDao;
import com.lawmate.dto.ConsultDto;
import com.lawmate.dto.ConsultSchDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Object getConsultCountByUserId(String userId) {

        return null;
    }

    public Object updateConsult(ConsultDto upt) {

        return dao.updateConsult(upt)>0?"수정 성공":"수정 되지 않았습니다.";
    }

    public Object deleteConsult(int id) {
        return dao.deleteConsult(id)>0?"삭제 성공":"삭제가 되지 않았습니다";
    }

    public Object getLawyerConsultList(String userId) {

        return null;
    }

    public Object getConsultListByUserId(String userId) {

        return null;
    }
}