package com.carbonclick.tsttask.secretsanta.assignment.repository;

import com.carbonclick.tsttask.secretsanta.assignment.controller.response.YearResponse;
import com.carbonclick.tsttask.secretsanta.assignment.repository.dto.YearDto;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.AssignmentEntity;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.YearEntity;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.YearEntity_;
import com.carbonclick.tsttask.secretsanta.base.QueryService;
import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YearRepository {

    private final EntityManager entityManager;
    private final AssignmentMapper assignmentMapper;
    private final QueryService queryService;

    private static final BiFunction<CriteriaBuilder, Root<YearEntity>, CompoundSelection<YearResponse>>
        SELECT_YEAR_RESPONSE = (cb, from) -> cb.construct(YearResponse.class,
            from.get(YearEntity_.YEAR_ID),
            from.get(YearEntity_.TITLE),
            from.get(YearEntity_.CREATED_AT));

    @Transactional
    public YearResponse create(YearDto yearDto) {

        YearEntity newYear = YearEntity.builder()
                .title(yearDto.getTitle())
                .build();

        entityManager.persist(newYear);

        newYear.assignments(yearDto.getAssignments().stream()
                .map(t -> AssignmentEntity.builder()
                        .yearId(newYear.yearId())
                        .giverId(t.getGiverId())
                        .takerId(t.getTakerId())
                        .build())
                .collect(Collectors.toList()));

        entityManager.persist(newYear);

        return YearResponse.builder()
                .id(newYear.yearId())
                .title(newYear.title())
                .build();
    }

    public Page<YearResponse> list(PageRequest pageable) {
        return queryService.pageQueryBuilder(YearEntity.class, YearResponse.class)
                .select(SELECT_YEAR_RESPONSE)
                .orderBy((cb, from) -> Collections.singletonList(cb.desc(from.get(YearEntity_.CREATED_AT))))
                .build().getPage(pageable);
    }

    public YearResponse get(Long id) {
        YearEntity year = entityManager.getReference(YearEntity.class, id);
        return assignmentMapper.mapYearToResponse(year);
    }

    public Optional<YearResponse> findYearByTitle(String title) {
        return queryService.querybuilder(YearEntity.class, YearResponse.class)
                .select(SELECT_YEAR_RESPONSE)
                .where((cb, from) -> cb.equal(from.get(YearEntity_.TITLE), title))
                .findSingleResult();
    }
}
