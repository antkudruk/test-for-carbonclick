package com.carbonclick.tsttask.secretsanta.assignment.repository.entity;

import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Builder
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AssignmentEntity {
    @Id
    @GeneratedValue
    private Long assignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_id")
    private YearEntity year;

    @Column(name = "year_id", insertable=false, updatable=false)
    private Long yearId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id", insertable=false, updatable=false)
    private ParticipantEntity giver;

    @Column(name = "giver_id")
    private Long giverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taker_id", insertable=false, updatable=false)
    private ParticipantEntity taker;

    @Column(name = "taker_id")
    private Long takerId;
}
