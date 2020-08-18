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
public class AssignmentResponse {
    private final long id;
    private final AssignmentParticipantResponse giver;
    private final AssignmentParticipantResponse taker;
}
