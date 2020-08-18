package com.carbonclick.tsttask.secretsanta.participant.repository;

import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity_;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<ParticipantResponse> list(Pageable pageable) {
        return new PageImpl<>(
                getParticipants(pageable),
                pageable,
                countParticipants());
    }

    private List<ParticipantResponse> getParticipants(Pageable pageable) {
        CriteriaQuery<ParticipantResponse> cq = criteriaBuilder.createQuery(ParticipantResponse.class);
        Root<ParticipantEntity> from = cq.from(ParticipantEntity.class);

        cq.select(criteriaBuilder.construct(ParticipantResponse.class,
                from.get(ParticipantEntity_.PARTICIPANT_ID),
                from.get(ParticipantEntity_.FIRST_NAME),
                from.get(ParticipantEntity_.LAST_NAME),
                from.get(ParticipantEntity_.EMAIL)));

        TypedQuery<ParticipantResponse> q = entityManager.createQuery(cq)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        return q.getResultList();
    }

    private long countParticipants() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ParticipantEntity> from = cq.from(ParticipantEntity.class);
        cq.select(cb.count(from));

        return Optional.ofNullable(entityManager.createQuery(cq).getSingleResult())
                .orElse(0L);
    }
}
