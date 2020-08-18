package com.carbonclick.tsttask.secretsanta.assignment.repository;

import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.AssignmentEntity;
import com.carbonclick.tsttask.secretsanta.base.BaseRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

@Service
public class AssignmentRepository extends BaseRepository<AssignmentEntity> {
    public AssignmentRepository(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {
        super(AssignmentEntity.class, entityManager, criteriaBuilder);
    }
}
