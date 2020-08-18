package com.carbonclick.tsttask.secretsanta.assignment.repository.entity;

import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Data
@Builder
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignment_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_id")
    private YearEntity year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id")
    private ParticipantEntity giver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taker_id")
    private ParticipantEntity taker;
}
