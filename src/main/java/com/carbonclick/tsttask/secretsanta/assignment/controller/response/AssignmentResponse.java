package com.carbonclick.tsttask.secretsanta.assignment.controller.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@ApiModel(description = "Assignment Info")
public class AssignmentResponse {
    private final long id;
    private final AssignmentParticipantResponse giver;
    private final AssignmentParticipantResponse taker;

    public AssignmentResponse(long id,
                              long giverId,
                              String giverFirstName,
                              String giverLastName,
                              long takerId,
                              String takerFirstName,
                              String takerLastName) {
        this.id = id;
        this.giver = new AssignmentParticipantResponse(giverId, giverFirstName, giverLastName);
        this.taker = new AssignmentParticipantResponse(takerId, takerFirstName, takerLastName);
    }
}
