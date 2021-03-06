package com.carbonclick.tsttask.secretsanta.assignment.controller.request;

import com.carbonclick.tsttask.secretsanta.assignment.service.UniqueYearNameService;
import com.carbonclick.tsttask.secretsanta.base.validation.ServiceValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Request to create and run new Secret Santa distribution")
@ServiceValidation({UniqueYearNameService.class})
public class NewAssignmentRequest {
    @ApiModelProperty("Title of the distribution (For instance, year number)")
    @NotBlank(message = "Year title should be specified")
    private String title;
    @ApiModelProperty("List of participant ids")
    @Size(min = 2, message = "Participants list should contain at least two participants")
    private Set<Long> participants;
}
