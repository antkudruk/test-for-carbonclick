package com.carbonclick.tsttask.secretsanta.assignment.repository;

import com.carbonclick.tsttask.secretsanta.assignment.controller.response.YearResponse;
import com.carbonclick.tsttask.secretsanta.assignment.repository.dto.YearDto;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.AssignmentEntity;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.YearEntity;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.YearEntity_;
import com.carbonclick.tsttask.secretsanta.base.BaseRepository;
import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class YearRepository extends BaseRepository<YearEntity> {

    private final AssignmentMapper assignmentMapper;

    public YearRepository(EntityManager entityManager,
                          CriteriaBuilder criteriaBuilder,
                          AssignmentMapper assignmentMapper) {
        super(YearEntity.class, entityManager, criteriaBuilder);
        this.assignmentMapper = assignmentMapper;
    }

    @Transactional
    public YearResponse create(YearDto yearDto) {

        List<AssignmentEntity> newAssignments = yearDto.getAssignments().stream()
                .map(t -> AssignmentEntity.builder()
                        .giverId(t.getGiverId())
                        .takerId(t.getTakerId())
                        .build())
                .collect(Collectors.toList());

        YearEntity newYear = YearEntity.builder()
                .title(yearDto.getTitle())
                .assignments(newAssignments)
                .build();

        getEntityManager().persist(newYear);
        getEntityManager().flush();

        return YearResponse.builder()
                .id(newYear.yearId())
                .title(newYear.title())
                .build();
    }

    public Page<YearResponse> list(PageRequest pageable) {
        return selectAsPage(
                YearResponse.class,
                pageable,
                (cb, from) -> getCriteriaBuilder().construct(YearResponse.class,
                        from.get(YearEntity_.YEAR_ID),
                        from.get(YearEntity_.TITLE)),
                (cb, from) -> Collections.singletonList(cb.desc(from.get(YearEntity_.CREATED_AT)))
        );
    }

    public YearResponse get(Long id) {
        YearEntity year = getEntityManager().getReference(YearEntity.class, id);
        return assignmentMapper.mapYearToResponse(year);
    }
}
