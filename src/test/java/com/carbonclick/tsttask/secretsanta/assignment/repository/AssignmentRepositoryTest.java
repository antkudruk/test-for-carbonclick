package com.carbonclick.tsttask.secretsanta.assignment.repository;

import com.carbonclick.tsttask.secretsanta.assignment.controller.response.AssignmentResponse;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.AssignmentEntity;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.YearEntity;
import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.repository.entity.ParticipantEntity;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AssignmentRepositoryTest {

    private static final int PAGE_SIZE = 3;
    private static final String YEAR_NAME = "Year Name";

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Test
    @Transactional
    @Rollback
    public void pageTest() {

        YearEntity yearEntity = createYearEntity();
        ParticipantEntity giver = createParticipantEntity(1);
        ParticipantEntity taker = createParticipantEntity(2);

        AssignmentEntity assignmentEntity = AssignmentEntity.builder()
                .yearId(yearEntity.yearId())
                .giverId(giver.participantId())
                .takerId(taker.participantId())
                .build();

        entityManager.persist(assignmentEntity);

        Page<AssignmentResponse> page = assignmentRepository.page(yearEntity.yearId(),
                new PageRequest(0, PAGE_SIZE));

        assertEquals(PAGE_SIZE, page.getPageSize());
        assertEquals(1, page.getTotal());
        List<AssignmentResponse> content = page.getContent();
        assertEquals("FirstName1", content.get(0).getGiver().getFirstName());
        assertEquals("LastName1", content.get(0).getGiver().getLastName());
        assertEquals("FirstName2", content.get(0).getTaker().getFirstName());
        assertEquals("LastName2", content.get(0).getTaker().getLastName());
    }

    private YearEntity createYearEntity() {
        YearEntity yearEntity = YearEntity.builder()
                .title(YEAR_NAME)
                .build();

        entityManager.persist(yearEntity);

        return yearEntity;
    }

    private ParticipantEntity createParticipantEntity(int i) {
        ParticipantEntity participantEntity = ParticipantEntity.builder()
                .firstName("FirstName" + i)
                .lastName("LastName" + i)
                .email("user" + i + "@host.dmn")
                .build();

        entityManager.persist(participantEntity);

        return participantEntity;
    }
}
