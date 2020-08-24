package com.carbonclick.tsttask.secretsanta.participant.controller.service;

import com.carbonclick.tsttask.secretsanta.base.validation.ValidatorService;
import com.carbonclick.tsttask.secretsanta.participant.controller.request.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UniqueFirstLastValidationService implements ValidatorService<ParticipantRequest> {
    private final ParticipantRepository participantRepository;

    @Override
    public String getMessage() {
        return "First and last name should be unique";
    }

    @Override
    public boolean isValid(ParticipantRequest object) {
        return !participantRepository.findByFirstLast(object.getFirstName(), object.getLastName()).isPresent();
    }

    @Override
    public List<String> getFieldName() {
        return Arrays.asList("firstName", "lastName");
    }
}
