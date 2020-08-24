package com.carbonclick.tsttask.secretsanta.participant.controller.request;

import com.carbonclick.tsttask.secretsanta.base.validation.ServiceValidation;
import com.carbonclick.tsttask.secretsanta.participant.controller.service.UniqueEmailValidationService;
import com.carbonclick.tsttask.secretsanta.participant.controller.service.UniqueFirstLastValidationService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Secret Santa Participant Request")
@Valid
@ServiceValidation({UniqueEmailValidationService.class, UniqueFirstLastValidationService.class})
public class ParticipantRequest {

    @ApiModelProperty("Participant First Name")
    @NotBlank(message = "First Name should be specified")
    private String firstName;

    @ApiModelProperty("Participant Last Name")
    @NotBlank(message = "Last Name should be specified")
    private String lastName;

    @ApiModelProperty("Participant Email")
    @NotBlank(message = "Email should be specified")
    private String email;
}
