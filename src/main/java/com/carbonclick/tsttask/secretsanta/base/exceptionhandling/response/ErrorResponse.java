package com.carbonclick.tsttask.secretsanta.base.exceptionhandling.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private final String error;
    private final List<FieldViolationResponse> fieldViolations;
}
