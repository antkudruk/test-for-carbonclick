package com.carbonclick.tsttask.secretsanta.assignment.repository;

import com.carbonclick.tsttask.secretsanta.assignment.controller.response.AssignmentResponse;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.AssignmentEntity;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.AssignmentEntity_;
import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity_;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class AssignmentRepository {

    private final QueryService queryService;

    public Page<AssignmentResponse> list(long yearId, PageRequest pageable) {
        return queryService.pageQueryBuilder(AssignmentEntity.class, AssignmentResponse.class)
                .select((cb, from) -> cb.construct(AssignmentResponse.class,
                        from.get(AssignmentEntity_.ASSIGNMENT_ID),

                        from.get(AssignmentEntity_.GIVER).get(ParticipantEntity_.PARTICIPANT_ID),
                        from.get(AssignmentEntity_.GIVER).get(ParticipantEntity_.FIRST_NAME),
                        from.get(AssignmentEntity_.GIVER).get(ParticipantEntity_.LAST_NAME),

                        from.get(AssignmentEntity_.TAKER).get(ParticipantEntity_.PARTICIPANT_ID),
                        from.get(AssignmentEntity_.TAKER).get(ParticipantEntity_.FIRST_NAME),
                        from.get(AssignmentEntity_.TAKER).get(ParticipantEntity_.LAST_NAME))
                )
                .where((cb, from) -> cb.equal(from.get(AssignmentEntity_.YEAR_ID), yearId))
                .orderBy((cb, from) -> Arrays.asList(
                        cb.asc(from.get(AssignmentEntity_.TAKER).get(ParticipantEntity_.FIRST_NAME)),
                        cb.asc(from.get(AssignmentEntity_.TAKER).get(ParticipantEntity_.LAST_NAME))
                ))
                .build().getPage(pageable);
    }
}
