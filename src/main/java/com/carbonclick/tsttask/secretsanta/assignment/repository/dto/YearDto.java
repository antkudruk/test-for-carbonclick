package com.carbonclick.tsttask.secretsanta.assignment.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class YearDto {
    private final String title;
    private final List<AssignmentDto> assignments;
}
