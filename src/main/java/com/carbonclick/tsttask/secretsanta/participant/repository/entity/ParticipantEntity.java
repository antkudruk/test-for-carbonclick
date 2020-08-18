package com.carbonclick.tsttask.secretsanta.participant.repository.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@Entity
public class ParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;
    private String firstName;
    private String lastName;
    private String email;
}
