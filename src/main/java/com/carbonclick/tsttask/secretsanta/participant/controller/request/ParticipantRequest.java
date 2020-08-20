package com.carbonclick.tsttask.secretsanta.participant.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Secret Santa Participant Request")
public class ParticipantRequest {

    @ApiModelProperty("Participant First Name")
    private String firstName;

    @ApiModelProperty("Participant Last Name")
    private String lastName;

    @ApiModelProperty("Participant Email")
    private String email;
}
