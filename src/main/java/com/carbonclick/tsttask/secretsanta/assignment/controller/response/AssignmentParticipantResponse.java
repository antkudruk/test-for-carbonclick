package com.carbonclick.tsttask.secretsanta.assignment.controller.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
@Getter
@ApiModel(description = "Assignment Info")
public class AssignmentParticipantResponse {
    private final long id;
    private final String firstName;
    private final String lastName;
}
