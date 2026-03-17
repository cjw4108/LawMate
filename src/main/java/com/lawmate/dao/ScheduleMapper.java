package com.lawmate.dao;


import com.lawmate.dto.ScheduleRequestDto;
import com.lawmate.dto.ScheduleResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    void insertSchedule(ScheduleRequestDto dto);
    Long getLastInsertId();
    List<ScheduleResponseDto> selectByUserId(String userId);
}
