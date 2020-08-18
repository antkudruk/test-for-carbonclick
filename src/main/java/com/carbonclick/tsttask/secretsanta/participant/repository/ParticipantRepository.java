package com.carbonclick.tsttask.secretsanta.participant.repository;

import com.carbonclick.tsttask.secretsanta.base.Page;
import com.carbonclick.tsttask.secretsanta.base.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.mapper.ParticipantMapper;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity_;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ParticipantRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    private final ParticipantMapper participantMapper;

    public Page<ParticipantResponse> list(PageRequest pageable) {
        return new Page<>(
                getParticipants(pageable),
                pageable.getPageSize(),
                countParticipants());
    }

    // TODO: Implement
    @Transactional
    public ParticipantResponse save(ParticipantRequest request) {
        ParticipantEntity participantEntity = participantMapper.map(new ParticipantEntity(), request);
        entityManager.persist(participantEntity);
        return participantMapper.map(participantEntity);
    }

    @Transactional
    public ParticipantResponse update(long id, ParticipantRequest request) {
        ParticipantEntity participantEntity = participantMapper.map(
                entityManager.getReference(ParticipantEntity.class, id),
                request);
        entityManager.persist(participantEntity);
        return participantMapper.map(participantEntity);
    }

    private List<ParticipantResponse> getParticipants(PageRequest pageable) {
        CriteriaQuery<ParticipantResponse> cq = criteriaBuilder.createQuery(ParticipantResponse.class);
        Root<ParticipantEntity> from = cq.from(ParticipantEntity.class);

        cq.select(criteriaBuilder.construct(ParticipantResponse.class,
                from.get(ParticipantEntity_.PARTICIPANT_ID),
                from.get(ParticipantEntity_.FIRST_NAME),
                from.get(ParticipantEntity_.LAST_NAME),
                from.get(ParticipantEntity_.EMAIL)));

        TypedQuery<ParticipantResponse> q = entityManager.createQuery(cq)
                .setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        return q.getResultList();
    }

    private int countParticipants() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ParticipantEntity> from = cq.from(ParticipantEntity.class);
        cq.select(cb.count(from));

        return Optional.ofNullable(entityManager.createQuery(cq).getSingleResult())
                .map(Long::intValue)
                .orElse(0);
    }

    public ParticipantResponse get(long id) {
        CriteriaQuery<ParticipantResponse> cq = criteriaBuilder.createQuery(ParticipantResponse.class);
        Root<ParticipantEntity> from = cq.from(ParticipantEntity.class);
        cq.select(criteriaBuilder.construct(ParticipantResponse.class,
                from.get(ParticipantEntity_.PARTICIPANT_ID),
                from.get(ParticipantEntity_.FIRST_NAME),
                from.get(ParticipantEntity_.LAST_NAME),
                from.get(ParticipantEntity_.EMAIL)))
        .where(criteriaBuilder.equal(from.get(ParticipantEntity_.PARTICIPANT_ID), id));

        TypedQuery<ParticipantResponse> q = entityManager.createQuery(cq);

        return q.getSingleResult();
    }
}
