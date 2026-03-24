package com.lawmate.service;

import com.lawmate.Chatting.ChatMapper;
import com.lawmate.dto.LawyerDTO;
import com.lawmate.dao.LawyerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 변호사 서비스 구현체
 */
@Service
public class LawyerService {

    @Autowired(required = false)
    private LawyerDAO lawyerDAO;

    @Autowired // 2. 채팅 상태 확인을 위해 ChatMapper 주입
    private ChatMapper chatMapper;

    public List<LawyerDTO> getLawyerList(LawyerDTO dto) {
        if (dto.getPageNo() <= 0) dto.setPageNo(1);
        if (dto.getPageSize() <= 0) dto.setPageSize(10);

        List<LawyerDTO> list = lawyerDAO.selectLawyerList(dto);

        if (list != null) {
            for (LawyerDTO lawyer : list) {
                // lawyerId(고유번호)를 사용하여 활성 상담 수 조회
                int activeCount = chatMapper.countActiveConsultations(lawyer.getLawyerId() + "");
                lawyer.setConsulting(activeCount > 0);
            }
        }
        return list;
    }

    public int getLawyerCount(LawyerDTO dto) {
        return lawyerDAO.selectLawyerCount(dto);
    }

    public LawyerDTO getLawyerDetail(Long lawyerId) {
        LawyerDTO result = lawyerDAO.selectLawyerDetail(lawyerId);
//        if (result == null) {
//            throw new RuntimeException("해당 변호사를 찾을 수 없습니다. ID: " + lawyerId);
//        }
        return result;
    }
    public LawyerDTO getLawyerByEmail(String email) {
        return lawyerDAO.selectLawyerByEmail(email);
    }
    public void registerLawyer(LawyerDTO dto) {
        if (dto.getStatus() == null || dto.getStatus().isEmpty()) {
            dto.setStatus("ACTIVE");
        }
        int result = lawyerDAO.insertLawyer(dto);
        if (result <= 0) {
            throw new RuntimeException("변호사 등록에 실패하였습니다.");
        }
    }

    public void modifyLawyer(LawyerDTO dto) {
        int result = lawyerDAO.updateLawyer(dto);
        if (result <= 0) {
            throw new RuntimeException("변호사 수정에 실패하였습니다.");
        }
    }

    public void removeLawyer(Long lawyerId) {
        int result = lawyerDAO.deleteLawyer(lawyerId);
        if (result <= 0) {
            throw new RuntimeException("변호사 삭제에 실패하였습니다.");
        }
    }
}
