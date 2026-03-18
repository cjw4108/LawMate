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
        return dto.getScheduleId();
    }

    @Transactional
    public void update(ScheduleRequestDto dto) {
        scheduleMapper.updateSchedule(dto);
    }

    @Transactional
    public void delete(Long scheduleId) {
        scheduleMapper.deleteSchedule(scheduleId);
    }

    public List<ScheduleResponseDto> findAll(String userId) {
        return scheduleMapper.selectByUserId(userId);
    }
}
