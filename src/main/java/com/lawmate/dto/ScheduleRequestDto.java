package com.lawmate.dto;

import lombok.Data;

@Data
public class ScheduleRequestDto {
    private String userId;
    private String title;
    private String content;
    private String startTime;
    private String endTime;
    private String clientName;
    private String status;
    private String color;
    private String allDay;
}
