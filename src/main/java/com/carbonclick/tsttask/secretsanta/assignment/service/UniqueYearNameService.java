package com.carbonclick.tsttask.secretsanta.assignment.service;

import com.carbonclick.tsttask.secretsanta.assignment.controller.request.NewAssignmentRequest;
import com.carbonclick.tsttask.secretsanta.assignment.repository.YearRepository;
import com.carbonclick.tsttask.secretsanta.base.validation.ValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@Component
@RequiredArgsConstructor
public class UniqueYearNameService implements ValidatorService<NewAssignmentRequest> {

    private final YearRepository yearRepository;

    @Override
    public String getMessage() {
        return "Year name should be unique";
    }

    @Override
    public boolean isValid(NewAssignmentRequest object) {
        return !yearRepository.findYearByTitle(object.getTitle()).isPresent();
    }

    @Override
    public List<String> getFieldName() {
        return Collections.singletonList("title");
    }
}
