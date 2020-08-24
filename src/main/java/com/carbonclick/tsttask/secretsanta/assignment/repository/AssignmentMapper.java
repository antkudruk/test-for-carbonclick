package com.carbonclick.tsttask.secretsanta.assignment.repository;

import com.carbonclick.tsttask.secretsanta.assignment.controller.response.YearResponse;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.YearEntity;
import org.springframework.stereotype.Service;

@Service
public class AssignmentMapper {
    public YearResponse mapYearToResponse(YearEntity e) {
        return YearResponse.builder()
                .id(e.yearId())
                .title(e.title())
                .createdAt(e.createdAt())
                .build();
    }
}
