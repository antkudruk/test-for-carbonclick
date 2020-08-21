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
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ParticipantRepository {

    private final ParticipantMapper participantMapper;
    private final EntityManager entityManager;
    private final QueryService queryService;

    public Page<ParticipantResponse> list(PageRequest pageable) {
        return queryService.pageQueryBuilder(ParticipantEntity.class, ParticipantResponse.class)
                .select((cb, from) -> cb.construct(ParticipantResponse.class,
                        from.get(ParticipantEntity_.PARTICIPANT_ID),
                        from.get(ParticipantEntity_.FIRST_NAME),
                        from.get(ParticipantEntity_.LAST_NAME),
                        from.get(ParticipantEntity_.EMAIL)))
                .orderBy((cb, from) -> Arrays.asList(
                        cb.asc(from.get(ParticipantEntity_.FIRST_NAME)),
                        cb.asc(from.get(ParticipantEntity_.LAST_NAME))
                ))
                .build().getPage(pageable);
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
