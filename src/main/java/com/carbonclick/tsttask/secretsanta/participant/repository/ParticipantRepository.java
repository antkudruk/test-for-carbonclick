package com.carbonclick.tsttask.secretsanta.participant.repository;

import com.carbonclick.tsttask.secretsanta.base.QueryService;
import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.request.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.mapper.ParticipantMapper;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity_;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class ParticipantRepository {

    private final ParticipantMapper participantMapper;
    private final EntityManager entityManager;
    private final QueryService queryService;

    private static final BiFunction<CriteriaBuilder, Root<ParticipantEntity>, CompoundSelection<ParticipantResponse>>
        SELECT_PARTICIPANT = (cb, from) -> cb.construct(ParticipantResponse.class,
            from.get(ParticipantEntity_.PARTICIPANT_ID),
            from.get(ParticipantEntity_.FIRST_NAME),
            from.get(ParticipantEntity_.LAST_NAME),
            from.get(ParticipantEntity_.EMAIL));

    public Page<ParticipantResponse> page(PageRequest pageable) {
        return queryService.pageQueryBuilder(ParticipantEntity.class, ParticipantResponse.class)
                .select(SELECT_PARTICIPANT)
                .orderBy((cb, from) -> Arrays.asList(
                        cb.asc(from.get(ParticipantEntity_.FIRST_NAME)),
                        cb.asc(from.get(ParticipantEntity_.LAST_NAME))
                ))
                .build().getPage(pageable);
    }

    public Optional<ParticipantResponse> findByFirstLast(String first, String last) {
        return queryService.querybuilder(ParticipantEntity.class, ParticipantResponse.class)
                .select(SELECT_PARTICIPANT)
                .where((cb, from) -> cb.and(
                        cb.equal(from.get(ParticipantEntity_.FIRST_NAME), first),
                        cb.equal(from.get(ParticipantEntity_.LAST_NAME), last)))
                .findSingleResult();
    }

    public Optional<ParticipantResponse> findByEmail(String email) {
        return queryService.querybuilder(ParticipantEntity.class, ParticipantResponse.class)
                .select(SELECT_PARTICIPANT)
                .where((cb, from) -> cb.and(cb.equal(from.get(ParticipantEntity_.EMAIL), email)))
                .findSingleResult();
    }

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

    public ParticipantResponse get(long id) {
        return queryService.querybuilder(ParticipantEntity.class, ParticipantResponse.class)
                .select((cb, from) -> cb.construct(ParticipantResponse.class,
                        from.get(ParticipantEntity_.PARTICIPANT_ID),
                        from.get(ParticipantEntity_.FIRST_NAME),
                        from.get(ParticipantEntity_.LAST_NAME),
                        from.get(ParticipantEntity_.EMAIL)))
                .where((cb, from) -> cb.equal(from.get(ParticipantEntity_.PARTICIPANT_ID), id))
                .build()
                .getSingleResult();
    }
}
