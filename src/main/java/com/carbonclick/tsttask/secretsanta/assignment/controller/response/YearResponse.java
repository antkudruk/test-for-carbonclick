package com.carbonclick.tsttask.secretsanta.assignment.controller.response;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Getter
@ApiModel(description = "Year Info")
public class YearResponse {
    private final long id;
    private final String title;
}
