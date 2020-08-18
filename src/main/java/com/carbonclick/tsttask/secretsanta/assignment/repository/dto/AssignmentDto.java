package com.carbonclick.tsttask.secretsanta.assignment.repository.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignmentDto {
    private final long giverId;
    private final long takerId;
}
