package com.lawmate.controller;

import com.lawmate.dto.ScheduleRequestDto;
import com.lawmate.dto.ScheduleResponseDto;
import com.lawmate.dto.UserDTO;
import com.lawmate.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ScheduleRequestDto dto,
                                    HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        dto.setUserId(loginUser.getUserId());
        Long id = scheduleService.save(dto);
        return ResponseEntity.ok(Map.of("scheduleId", id));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getAll(HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        return ResponseEntity.ok(scheduleService.findAll(loginUser.getUserId()));
    }
}
