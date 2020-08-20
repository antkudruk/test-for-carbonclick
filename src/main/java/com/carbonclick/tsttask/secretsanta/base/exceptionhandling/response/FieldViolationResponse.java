package com.carbonclick.tsttask.secretsanta.base.exceptionhandling.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FieldViolationResponse {
    private final String objectName;
    private final String fieldName;
    private final String message;
}
