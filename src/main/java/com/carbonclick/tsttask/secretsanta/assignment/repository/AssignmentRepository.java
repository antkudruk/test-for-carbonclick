package com.carbonclick.tsttask.secretsanta.assignment.repository;

import com.carbonclick.tsttask.secretsanta.assignment.controller.response.AssignmentResponse;
import com.carbonclick.tsttask.secretsanta.assignment.repository.dto.AssignmentDto;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.AssignmentEntity;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.AssignmentEntity_;
import com.carbonclick.tsttask.secretsanta.base.BaseRepository;
import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity_;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

@Service
public class AssignmentRepository extends BaseRepository<AssignmentEntity> {
    public AssignmentRepository(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {
        super(AssignmentEntity.class, entityManager, criteriaBuilder);
    }

    public Page<AssignmentResponse> list(long yearId, PageRequest pageable) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AssignmentResponse> cq = getCriteriaBuilder().createQuery(AssignmentResponse.class);
        Root<AssignmentEntity> from = cq.from(AssignmentEntity.class);

        Join<AssignmentEntity, ParticipantEntity> giverParticipantJoin
                = from.join(AssignmentEntity_.GIVER);

        Join<AssignmentEntity, ParticipantEntity> takerParticipantJoin
                = from.join(AssignmentEntity_.TAKER);

        cq.select(cb.construct(AssignmentResponse.class,
                from.get(AssignmentEntity_.ASSIGNMENT_ID),

                from.get(AssignmentEntity_.GIVER).get(ParticipantEntity_.PARTICIPANT_ID),
                from.get(AssignmentEntity_.GIVER).get(ParticipantEntity_.FIRST_NAME),
                from.get(AssignmentEntity_.GIVER).get(ParticipantEntity_.LAST_NAME),

                from.get(AssignmentEntity_.TAKER).get(ParticipantEntity_.PARTICIPANT_ID),
                from.get(AssignmentEntity_.TAKER).get(ParticipantEntity_.FIRST_NAME),
                from.get(AssignmentEntity_.TAKER).get(ParticipantEntity_.LAST_NAME))
        );

        cq.where(cb.equal(from.get(AssignmentEntity_.YEAR_ID), yearId));

        cq.orderBy(cb.asc(cb.concat(
                ((Join)takerParticipantJoin).get(ParticipantEntity_.FIRST_NAME),
                ((Join)takerParticipantJoin).get(ParticipantEntity_.LAST_NAME))));

        TypedQuery<AssignmentResponse> q = getEntityManager()
                .createQuery(cq)
                .setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        return new Page<>(
                q.getResultList(),
                pageable.getPageSize(),
                count());
    }

    @Transactional
    public AssignmentDto saveAssignment(AssignmentDto assignmentDto) {
        AssignmentEntity assignmentEntity = AssignmentEntity.builder()
                .giverId(assignmentDto.getGiverId())
                .takerId(assignmentDto.getTakerId())
                .build();

        getEntityManager().persist(assignmentEntity);

        return AssignmentDto.builder()
                .giverId(assignmentEntity.giverId())
                .takerId(assignmentEntity.takerId())
                .build();
    }
}
