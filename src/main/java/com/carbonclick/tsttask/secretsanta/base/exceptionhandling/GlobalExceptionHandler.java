package com.carbonclick.tsttask.secretsanta.base.exceptionhandling;

import com.carbonclick.tsttask.secretsanta.base.exceptionhandling.response.ErrorResponse;
import com.carbonclick.tsttask.secretsanta.base.exceptionhandling.response.FieldViolationResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<FieldViolationResponse> fieldViolations = Optional.ofNullable(exception)
                .map(MethodArgumentNotValidException::getBindingResult)
                .map(Errors::getAllErrors)
                .orElse(new ArrayList<>())
                .stream()
                .map(e -> (FieldError)e)
                .map(e -> FieldViolationResponse.builder()
                        .message(e.getDefaultMessage())// TODO: Is really DEFAULT message?
                        .objectName(e.getObjectName())
                        .fieldName(e.getField())
                        .build())
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(Optional.ofNullable(exception)
                        .map(MethodArgumentNotValidException::getMessage)
                        .orElse(""))
                .fieldViolations(fieldViolations)
                .build();

        return new ResponseEntity<>(errorResponse, headers, status);
    }
}
