package com.lawmate.Chatting;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class A02_ChattingController {

    @GetMapping("/ai/consult")
    public String aiConsult(Model model) {

        model.addAttribute("roomId", UUID.randomUUID().toString());
        model.addAttribute("userType", "USER");
        model.addAttribute("socketServer", "ws://localhost:8080/chatSocket");

        return "ai/aiConsult";
    }
}