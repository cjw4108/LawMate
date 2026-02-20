package com.lawmate.dao;

import com.lawmate.dto.LawyerApprovalDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminLawyerDAO {

    // 임시 메모리 (DB 전환 가능)
    private final List<LawyerApprovalDTO> lawyers = new ArrayList<>();

    public AdminLawyerDAO() {
        LawyerApprovalDTO l1 = new LawyerApprovalDTO();
        l1.setLawyerId("lawyer02");
        l1.setLawyerName("김변호");
        l1.setEmail("lawyer02@lawmate.com");
        l1.setStatus("PENDING");

        lawyers.add(l1);
    }

    /* 승인 대기 목록 */
    public List<LawyerApprovalDTO> findPendingLawyers() {
        List<LawyerApprovalDTO> result = new ArrayList<>();
        for (LawyerApprovalDTO l : lawyers) {
            if ("PENDING".equals(l.getStatus())) {
                result.add(l);
            }
        }
        return result;
    }

    /* 승인 */
    public void approve(String lawyerId) {
        for (LawyerApprovalDTO l : lawyers) {
            if (l.getLawyerId().equals(lawyerId)) {
                l.setStatus("APPROVED");
            }
        }
    }

    /* 반려 */
    public void reject(String lawyerId) {
        for (LawyerApprovalDTO l : lawyers) {
            if (l.getLawyerId().equals(lawyerId)) {
                l.setStatus("REJECTED");
            }
        }
    }
}