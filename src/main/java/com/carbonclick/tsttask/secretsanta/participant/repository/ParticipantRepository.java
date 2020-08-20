package com.carbonclick.tsttask.secretsanta.participant.repository;

import com.carbonclick.tsttask.secretsanta.base.BaseRepository;
import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.request.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.mapper.ParticipantMapper;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity_;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Service
public class ParticipantRepository extends BaseRepository<ParticipantEntity> {

    private final ParticipantMapper participantMapper;

    public ParticipantRepository(
            EntityManager entityManager,
            CriteriaBuilder criteriaBuilder,
            ParticipantMapper participantMapper) {
        super(ParticipantEntity.class, entityManager, criteriaBuilder);
        this.participantMapper = participantMapper;
    }

    public Page<ParticipantResponse> list(PageRequest pageable) {
        return selectAsPage(
                ParticipantResponse.class,
                pageable,
                (cb, from) -> getCriteriaBuilder().construct(ParticipantResponse.class,
                        from.get(ParticipantEntity_.PARTICIPANT_ID),
                        from.get(ParticipantEntity_.FIRST_NAME),
                        from.get(ParticipantEntity_.LAST_NAME),
                        from.get(ParticipantEntity_.EMAIL))
        );
    }

    @Transactional
    public ParticipantResponse save(ParticipantRequest request) {
        ParticipantEntity participantEntity = participantMapper.map(new ParticipantEntity(), request);
        getEntityManager().persist(participantEntity);
        return participantMapper.map(participantEntity);
    }

    @Transactional
    public ParticipantResponse update(long id, ParticipantRequest request) {
        ParticipantEntity participantEntity = participantMapper.map(
                getEntityManager().getReference(ParticipantEntity.class, id),
                request);
        getEntityManager().persist(participantEntity);
        return participantMapper.map(participantEntity);
    }

    public ParticipantResponse get(long id) {
        CriteriaQuery<ParticipantResponse> cq = getCriteriaBuilder().createQuery(ParticipantResponse.class);
        Root<ParticipantEntity> from = cq.from(ParticipantEntity.class);
        cq.select(getCriteriaBuilder().construct(ParticipantResponse.class,
                from.get(ParticipantEntity_.PARTICIPANT_ID),
                from.get(ParticipantEntity_.FIRST_NAME),
                from.get(ParticipantEntity_.LAST_NAME),
                from.get(ParticipantEntity_.EMAIL)))
        .where(getCriteriaBuilder().equal(from.get(ParticipantEntity_.PARTICIPANT_ID), id));

        TypedQuery<ParticipantResponse> q = getEntityManager().createQuery(cq);

        return q.getSingleResult();
    }
}
