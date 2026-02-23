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
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("consultDetail")
    public String consultDetail(@RequestParam("id") int id, Model d) {
        d.addAttribute("consult", service.getConsult(id));
        return "consult\\consultDetail";
    }

    @PostMapping("updateConsult")
    public String updateConsult(ConsultDto upt, Model d) {
        d.addAttribute("msg", service.updateConsult(upt));
        // 수정된 이후 내용을 상세화면에서 확인..
        d.addAttribute("consult", service.getConsult(upt.getId()));
        return "consult\\consultDetail";
    }

    @PostMapping("deleteConsult")
    public String deleteConsult(@RequestParam("id") int id, Model d) {
        System.out.println("id = " + id + ";");
        d.addAttribute("msg", service.deleteConsult(id) );
        return "consult\\consultDetail";
    }
}
