package com.lawmate.controller;

import com.lawmate.dao.CartDAO;
import com.lawmate.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

    @Autowired
    private CartDAO cartDAO; // ⭐ CartController와 동일하게 DAO를 직접 주입

    @GetMapping({"/mypage", "/mypage/**"})
    public String mypage(HttpSession session, Model model) {
        // 1. 세션에서 로그인 유저 정보 가져오기
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser != null) {
            String userId = loginUser.getUserId();

            // 2. CartDAO를 사용하여 서류함 개수 조회
            int count = cartDAO.countCartByUserId(userId);

            // 3. 모델에 담아 header.jsp로 전달
            // 💡 만약 header.jsp에서 다른 이름(예: totalDocs)을 쓴다면 그 이름으로 바꿔주세요.
            model.addAttribute("cartCount", count);

            System.out.println("=== 마이페이지 데이터 로드 ===");
            System.out.println("로그인 유저: " + userId);
            System.out.println("서류함 개수: " + count);
        }

        return "mypage";
    }
}