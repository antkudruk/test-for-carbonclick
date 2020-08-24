package com.carbonclick.tsttask.secretsanta.assignment.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private Long yearId;
    private String title;
    @Builder.Default
    @OneToMany(mappedBy = "year", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentEntity> assignments = new ArrayList<>();

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
