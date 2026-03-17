package com.lawmate.service;

import com.lawmate.dao.ScheduleMapper;
import com.lawmate.dto.ScheduleRequestDto;
import com.lawmate.dto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleMapper scheduleMapper;

    @Transactional
    public Long save(ScheduleRequestDto dto) {
        scheduleMapper.insertSchedule(dto);
        return scheduleMapper.getLastInsertId();
    }

    public List<ScheduleResponseDto> findAll(String userId) {
        return scheduleMapper.selectByUserId(userId);
    }
}
