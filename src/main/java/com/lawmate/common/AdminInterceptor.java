package com.lawmate.common;

import jakarta.servlet.http.HttpServletRequest;  // javax -> jakarta로 변경
import jakarta.servlet.http.HttpServletResponse; // javax -> jakarta로 변경
import jakarta.servlet.http.HttpSession;         // javax -> jakarta로 변경
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");

        // ROLE_ADMIN이 아니면 차단하고 로그인으로 보내야 함
        if (userRole == null || !"ROLE_ADMIN".equals(userRole)) {
            response.sendRedirect("/login"); // 튕겨내기
            return false; // 컨트롤러 진입 차단
        }
        return true; // 관리자만 통과
    }
}