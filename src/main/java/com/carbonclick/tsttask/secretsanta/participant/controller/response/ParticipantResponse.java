package com.carbonclick.tsttask.secretsanta.participant.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantResponse {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;
}
