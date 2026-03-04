package com.lawmate.config;

import com.lawmate.dto.AdminDTO;
import com.lawmate.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.PrintWriter;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        boolean isAdmin = false;

        if (session != null) {
            AdminDTO loginAdmin = (AdminDTO) session.getAttribute("loginAdmin");
            if (loginAdmin != null) isAdmin = true;

            UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
            if (loginUser != null && "ROLE_ADMIN".equals(loginUser.getRole())) isAdmin = true;
        }

        if (isAdmin) return true;

        // 권한 없을 때: 화면을 비우고 /home으로 강제 이동
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'></head>");
        out.println("<body style='background-color:white;'>");
        out.println("<script>");
        out.println("  alert('관리자 권한이 없습니다.');");
        // 핵심 수정: 이동 경로를 프로젝트의 실제 메인인 /home으로 설정
        out.println("  window.location.href = '" + request.getContextPath() + "/home';");
        out.println("</script>");
        out.println("</body></html>");

        out.flush();
        out.close();

        return false;
    }
}