package com.carbonclick.tsttask.secretsanta.assignment.repository;

import com.carbonclick.tsttask.secretsanta.assignment.controller.response.AssignmentParticipantResponse;
import com.carbonclick.tsttask.secretsanta.assignment.controller.response.AssignmentResponse;
import com.carbonclick.tsttask.secretsanta.assignment.repository.dto.AssignmentDto;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.AssignmentEntity;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.AssignmentEntity_;
import com.carbonclick.tsttask.secretsanta.base.BaseRepository;
import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity_;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

@Service
public class AssignmentRepository extends BaseRepository<AssignmentEntity> {
    public AssignmentRepository(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {
        super(AssignmentEntity.class, entityManager, criteriaBuilder);
    }

    public Page<AssignmentResponse> list(PageRequest pageable) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AssignmentResponse> cq = getCriteriaBuilder().createQuery(AssignmentResponse.class);
        Root<AssignmentEntity> from = cq.from(AssignmentEntity.class);
        Join<AssignmentEntity, ParticipantEntity> takerParticipant
                = from.join(AssignmentEntity_.TAKER);
        Join<AssignmentEntity, ParticipantEntity> giverParticipant
                = from.join(AssignmentEntity_.GIVER);

        cq.select(cb.construct(AssignmentResponse.class,
                from.get(AssignmentEntity_.ASSIGNMENT_ID),
                cb.construct(AssignmentParticipantResponse.class,
                        takerParticipant.get(ParticipantEntity_.PARTICIPANT_ID),
                        takerParticipant.get(ParticipantEntity_.FIRST_NAME),
                        takerParticipant.get(ParticipantEntity_.LAST_NAME)),
                cb.construct(AssignmentParticipantResponse.class,
                        giverParticipant.get(ParticipantEntity_.PARTICIPANT_ID),
                        giverParticipant.get(ParticipantEntity_.FIRST_NAME),
                        giverParticipant.get(ParticipantEntity_.LAST_NAME))
                )
        );

        cq.orderBy(cb.asc(cb.concat(
                takerParticipant.get(ParticipantEntity_.FIRST_NAME),
                takerParticipant.get(ParticipantEntity_.LAST_NAME))));

        TypedQuery<AssignmentResponse> q = getEntityManager().createQuery(cq)
                .setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        return new Page<>(
                q.getResultList(),
                pageable.getPageSize(),
                countParticipants());
    }

    public AssignmentDto saveAssignment(AssignmentDto assignmentDto) {
        AssignmentEntity assignmentEntity = AssignmentEntity.builder()

                .build();

        getEntityManager().
    }
}
