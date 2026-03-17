package com.lawmate.controller;

import com.lawmate.Chatting.A03_ChattingService;
import com.lawmate.dto.LawyerDTO;
import com.lawmate.dto.UserDTO;
import com.lawmate.service.LawyerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 변호사 프로필 Controller
 */
@Controller
@RequestMapping("/lawyer")
public class LawyerController {

    @Autowired(required = false)
    private LawyerService lawyerService;

    @Autowired
    private A03_ChattingService chattingService;

    /**
     * 변호사 목록 조회
     * GET /lawyer/list
     */
    @GetMapping("/list")
    public String lawyerList(LawyerDTO dto, Model model, HttpSession session) {
        if (dto.getPageNo() <= 0) dto.setPageNo(1);
        if (dto.getPageSize() <= 0) dto.setPageSize(10);
        if (dto.getBlockSize() <= 0) dto.setBlockSize(5);
//        System.out.println("# process 11 #");
        int totalCount = lawyerService.getLawyerCount(dto);
        List<LawyerDTO> lawyerList = lawyerService.getLawyerList(dto);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser != null) {
            String userId = loginUser.getUserId();
            for (LawyerDTO lawyer : lawyerList) {
                if (lawyer.getEmail() != null) {
                    // 이메일에서 ID만 추출 (예: lawyer123)
                    String lawyerEmailId = lawyer.getEmail().split("@")[0];
                    // 기존에 생성된 방 ID가 있는지 조회
                    String existingRoomId = chattingService.findRoom(userId, lawyerEmailId);
                    // DTO에 담아줍니다 (JSP에서 사용)
                    lawyer.setExistingRoomId(existingRoomId);
                }
            }
        }

        // 페이징 계산
        int totalPages = (int) Math.ceil((double) totalCount / dto.getPageSize());
        int startPage  = Math.max(1, (dto.getPageNo() - 1) / dto.getBlockSize() * dto.getBlockSize() + 1);
        int endPage    = Math.min(totalPages, startPage + dto.getBlockSize() - 1);
        int last1Page  = (totalPages - 1) / dto.getBlockSize() * dto.getBlockSize() + 1;

        model.addAttribute("lawyerList",  lawyerList);
        model.addAttribute("totalCount",  totalCount);
        model.addAttribute("totalPages",  totalPages);
        model.addAttribute("startPage",   startPage);
        model.addAttribute("endPage",     endPage);
        model.addAttribute("last1Page",   last1Page);
        model.addAttribute("searchDTO",   dto);

        return "lawyer/lawyerList";
    }

    /**
     * 변호사 상세 조회
     * GET /lawyer/detail/{lawyerId}
     */
    @GetMapping("/detail/{lawyerId}")
    public String lawyerDetail(@PathVariable Long lawyerId, Model model) {
        LawyerDTO lawyer = lawyerService.getLawyerDetail(lawyerId);
        model.addAttribute("lawyer", lawyer);
        return "lawyer/lawyerDetail";
    }

    /**
     * 변호사 등록 폼
     * GET /lawyer/register
     */
    @GetMapping("/register")
    public String lawyerRegisterForm(Model model) {
        model.addAttribute("lawyer", new LawyerDTO());
        return "lawyer/lawyerForm";
    }

    /**
     * 변호사 등록 처리
     * POST /lawyer/register
     */
    @PostMapping("/register")
    public String lawyerRegister(@ModelAttribute LawyerDTO dto,
                                 RedirectAttributes redirectAttr) {
        try {
//            System.out.println("# process 14 #"+dto);
            lawyerService.registerLawyer(dto);
            redirectAttr.addFlashAttribute("successMsg", "변호사가 등록되었습니다.");
        } catch (Exception e) {
            String msg = e.getMessage();
            String errorMsg = msg.substring(msg.lastIndexOf(":") + 1);
            if(errorMsg.contains("PRJ01.SYS_C008609")) {errorMsg=errorMsg.replace("PRJ01.SYS_C008609","변호사 등록번호");}
            else if(errorMsg.contains("PRJ01.SYS_C008610")) {errorMsg=errorMsg.replace("PRJ01.SYS_C008610","이메일");}
            redirectAttr.addFlashAttribute("errorMsg", errorMsg);
            return "redirect:/lawyer/register";
        }
        return "redirect:/lawyer/list";
    }

    /**
     * 변호사 수정 폼
     * GET /lawyer/modify/{lawyerId}
     */
    @GetMapping("/modify/{lawyerId}")
    public String lawyerModifyForm(@PathVariable Long lawyerId, Model model) {
        LawyerDTO lawyer = lawyerService.getLawyerDetail(lawyerId);
        model.addAttribute("lawyer", lawyer);
        return "lawyer/lawyerForm";
    }

    /**
     * 변호사 수정 처리
     * POST /lawyer/modify
     */
    @PostMapping("/modify")
    public String lawyerModify(@ModelAttribute LawyerDTO dto,
                               RedirectAttributes redirectAttr) {
        try {
            lawyerService.modifyLawyer(dto);
            redirectAttr.addFlashAttribute("successMsg", "변호사 정보가 수정되었습니다.");
        } catch (Exception e) {
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/lawyer/detail/" + dto.getLawyerId();
    }

    /**
     * 변호사 삭제 처리
     * POST /lawyer/delete/{lawyerId}
     */
    @PostMapping("/delete/{lawyerId}")
    public String lawyerDelete(@PathVariable Long lawyerId,
                               RedirectAttributes redirectAttr) {
        try {
            lawyerService.removeLawyer(lawyerId);
            redirectAttr.addFlashAttribute("successMsg", "변호사가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttr.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/lawyer/list";
    }
}
