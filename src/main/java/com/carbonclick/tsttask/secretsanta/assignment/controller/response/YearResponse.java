package com.carbonclick.tsttask.secretsanta.assignment.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@Getter
@ApiModel(description = "Year Info")
public class YearResponse {
    private final long id;
    private final String title;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
}
