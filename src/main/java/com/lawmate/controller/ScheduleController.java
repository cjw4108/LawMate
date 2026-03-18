package com.lawmate.controller;

import com.lawmate.dto.ScheduleRequestDto;
import com.lawmate.dto.ScheduleResponseDto;
import com.lawmate.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import com.lawmate.service.ScheduleService;
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

        if (loginUser == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }

        dto.setUserId(loginUser.getUserId());
        Long id = scheduleService.save(dto);
        return ResponseEntity.ok(Map.of("scheduleId", id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }

        return ResponseEntity.ok(scheduleService.findAll(loginUser.getUserId()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody ScheduleRequestDto dto,
                                    HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        dto.setScheduleId(id);
        dto.setUserId(loginUser.getUserId());
        scheduleService.update(dto);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        scheduleService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}