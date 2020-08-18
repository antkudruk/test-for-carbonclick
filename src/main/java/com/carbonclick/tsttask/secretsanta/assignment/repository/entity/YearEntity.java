package com.carbonclick.tsttask.secretsanta.assignment.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class YearEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long year_id;
    private String title;
    @Builder.Default
    @OneToMany
    @JoinColumn(name = "year")
    private List<AssignmentEntity> assignments = new ArrayList<>();
    private Instant createdAt;
}
