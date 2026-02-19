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

    public List<ConsultDto> getConsultList(@NotNull ConsultSchDto sch){
        if(sch.getSchTitle()==null) {
            sch.setSchTitle("");
        }
        sch.setSchTitle("%"+sch.getSchTitle()+"%");
        System.out.println(sch.getSchTitle());
        return dao.getConsultList(sch);
    }

    public String consultInsert(ConsultDto ins) {

        return dao.insertConsult(ins)>0?"등록성공":"등록되지 않았습니다";
    }
}
