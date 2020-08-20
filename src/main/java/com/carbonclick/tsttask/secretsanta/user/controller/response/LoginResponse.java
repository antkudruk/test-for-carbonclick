package com.carbonclick.tsttask.secretsanta.user.controller.response;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Getter
@ApiModel(description = "Contains JWT token to authenticate")
public class LoginResponse {
    private final String token;
}
