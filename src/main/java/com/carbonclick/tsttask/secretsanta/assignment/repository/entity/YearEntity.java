package com.carbonclick.tsttask.secretsanta.assignment.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
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
    @GeneratedValue
    private Long yearId;
    private String title;
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "year")
    private List<AssignmentEntity> assignments = new ArrayList<>();
    private Instant createdAt;

    public YearEntity assignments(List<AssignmentEntity> assignments) {
        this.assignments.clear();
        this.assignments.addAll(assignments);
        this.assignments.forEach(t -> t.year(this));
        return this;
    }
}
