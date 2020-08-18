package com.carbonclick.tsttask.secretsanta.participant.controller.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
@Getter
@ApiModel(description = "Secret Santa Participant Response")
public class ParticipantResponse {
    @ApiModelProperty("Participant Identity")
    private final long id;

    @ApiModelProperty("Participant First Name")
    private final String firstName;

    @ApiModelProperty("Participant Last Name")
    private final String lastName;

    @ApiModelProperty("Participant Email")
    private final String email;
}
