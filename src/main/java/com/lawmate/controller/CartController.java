package com.lawmate.controller;

import com.lawmate.dao.CartDAO;
import com.lawmate.dto.CartDTO;
import com.lawmate.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    private CartDAO cartDAO;

    @GetMapping("/cart")
    public String cartPage(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        String userId = loginUser != null ? loginUser.getUserId() : null;

        if (userId == null) {
            return "redirect:/login";
        }

        List<CartDTO> cartList = cartDAO.selectCartByUserId(userId);
        model.addAttribute("cartList", cartList);
        return "cart";
    }

    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam Long documentId,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        String userId = loginUser != null ? loginUser.getUserId() : null;

        System.out.println("=== 장바구니 추가 시도 ===");
        System.out.println("userId: " + userId);
        System.out.println("documentId: " + documentId);

        if (userId == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return ResponseEntity.ok(result);
        }

        try {

            int exists = cartDAO.checkDuplicate(userId, documentId);
            if (exists > 0) {
                result.put("success", false);
                result.put("message", "이미 장바구니에 담긴 서류입니다.");
                return ResponseEntity.ok(result);
            }

            CartDTO cart = new CartDTO();
            cart.setUserId(userId);
            cart.setDocumentId(documentId);
            cartDAO.insertCart(cart);

            int count = cartDAO.countCartByUserId(userId);

            result.put("success", true);
            result.put("message", "장바구니에 담았습니다.");
            result.put("cartCount", count);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/api/cart/count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCartCount(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        String userId = loginUser != null ? loginUser.getUserId() : null;

        if (userId == null) {
            result.put("count", 0);
            return ResponseEntity.ok(result);
        }

        int count = cartDAO.countCartByUserId(userId);
        result.put("count", count);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/cart/delete/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCart(
            @PathVariable Long id,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        String userId = loginUser != null ? loginUser.getUserId() : null;

        if (userId == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return ResponseEntity.ok(result);
        }

        try {
            cartDAO.deleteCart(id, userId);
            int count = cartDAO.countCartByUserId(userId);

            result.put("success", true);
            result.put("message", "삭제되었습니다.");
            result.put("cartCount", count);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "오류가 발생했습니다.");
            return ResponseEntity.status(500).body(result);
        }
    }

    @DeleteMapping("/api/cart/clear")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> clearCart(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        String userId = loginUser != null ? loginUser.getUserId() : null;

        if (userId == null) {
            result.put("success", false);
            return ResponseEntity.ok(result);
        }

        cartDAO.deleteAllCart(userId);
        result.put("success", true);
        return ResponseEntity.ok(result);
    }
}