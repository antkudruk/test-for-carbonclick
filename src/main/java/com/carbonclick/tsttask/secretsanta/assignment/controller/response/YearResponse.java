package com.carbonclick.tsttask.secretsanta.assignment.controller.response;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@Getter
@ApiModel(description = "Year Info")
public class YearResponse {
    private final long id;
    private final String title;
    private final Instant createdAt;
}
