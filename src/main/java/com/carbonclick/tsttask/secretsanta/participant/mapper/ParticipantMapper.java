package com.carbonclick.tsttask.secretsanta.participant.mapper;

import com.carbonclick.tsttask.secretsanta.participant.controller.request.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity;
import org.springframework.stereotype.Service;

@Service
public class ParticipantMapper {

    public ParticipantEntity map(ParticipantEntity participantEntity, ParticipantRequest request) {
        return participantEntity
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail());
    }

    public ParticipantResponse map(ParticipantEntity entity) {
        return ParticipantResponse.builder()
                .id(entity.participantId())
                .firstName(entity.firstName())
                .lastName(entity.lastName())
                .email(entity.email())
                .build();
    }
}
