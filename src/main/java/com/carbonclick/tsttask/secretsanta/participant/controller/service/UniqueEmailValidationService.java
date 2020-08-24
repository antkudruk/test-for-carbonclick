package com.carbonclick.tsttask.secretsanta.participant.controller.service;

import com.carbonclick.tsttask.secretsanta.base.validation.ValidatorService;
import com.carbonclick.tsttask.secretsanta.participant.controller.request.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidationService implements ValidatorService<ParticipantRequest> {

    private final ParticipantRepository participantRepository;

    @Override
    public String getMessage() {
        return "Email should be unique";
    }

    @Override
    public boolean isValid(ParticipantRequest object) {
        return !participantRepository.findByEmail(object.getEmail()).isPresent();
    }

    @Override
    public List<String> getFieldName() {
        return Collections.singletonList("email");
    }
}
