package com.lawmate.controller;

import com.lawmate.dao.CartDAO;
import com.lawmate.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class SpaController {

    @Autowired
    private CartDAO cartDAO;

    @GetMapping({
            "/clients",
            "/schedule",
            "/documents",
            "/notifications",
            "/overview",
            "/user"
    })
    public String forwardToMypage(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser != null) {
            int count = cartDAO.countCartByUserId(loginUser.getUserId());
            model.addAttribute("cartCount", count);
        }
        return "mypage";
    }
}