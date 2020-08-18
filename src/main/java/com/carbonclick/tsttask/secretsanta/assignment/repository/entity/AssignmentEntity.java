package com.carbonclick.tsttask.secretsanta.assignment.repository.entity;

import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

@Data
@Builder
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AssignmentEntity {
    private YearEntity year;
    private ParticipantEntity giver;
    private ParticipantEntity taker;
}
