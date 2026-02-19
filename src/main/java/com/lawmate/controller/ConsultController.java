package com.lawmate.controller;

import com.lawmate.dto.ConsultDto;
import com.lawmate.dto.ConsultSchDto;
import com.lawmate.service.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/consult")
public class ConsultController {
    @Autowired(required = false)
    private ConsultService service;

    @RequestMapping("/consultList")
    public String consultList(ConsultSchDto sch, Model d) {
        d.addAttribute("consultList", service.getConsultList(sch));
        return "consult/consultList";}

    @GetMapping("consultInsert")
    public String consultInsertGet() {
        return "consult\\consultInsert";
    }

    @PostMapping("consultInsert")
    public String consultInsertPost(ConsultDto ins, Model d) {
        System.out.println(ins);
        d.addAttribute("msg", service.consultInsert(ins));

        return "consult\\consultInsert";
    }

}
