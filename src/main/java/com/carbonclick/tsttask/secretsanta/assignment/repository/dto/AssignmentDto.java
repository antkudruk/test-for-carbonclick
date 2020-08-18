package com.carbonclick.tsttask.secretsanta.assignment.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AssignmentDto {
    private final long giverId;
    private final long takerId;
}
