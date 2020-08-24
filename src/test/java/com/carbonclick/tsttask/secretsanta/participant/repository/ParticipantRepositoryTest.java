package com.carbonclick.tsttask.secretsanta.participant.repository;

import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.request.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParticipantRepositoryTest {

    private final int PAGE_SIZE = 3;
    private final String FIRST = "John";
    private final String LAST = "Smith";
    private final String EMAIL = "js@host.dmn";

    @Autowired
    private EntityManager entityManager;

    @Autowired
    protected ParticipantRepository participantRepository;

    @Test
    @Transactional
    @Rollback
    public void pageTest() {
        ParticipantEntity participantEntity = createParticipantEntity();

        Page<ParticipantResponse> page = participantRepository.page(new PageRequest(0, PAGE_SIZE));

        assertEquals(PAGE_SIZE, page.getPageSize());
        assertEquals(1, page.getTotal());

        compare(participantEntity, page.getContent().get(0));
    }

    @Test
    @Transactional
    @Rollback
    public void findByExistingFirstLastTest() {
        ParticipantEntity participantEntity = createParticipantEntity();
        compare(participantEntity, participantRepository.findByFirstLast(FIRST, LAST).orElseThrow(RuntimeException::new));
    }

    @Test
    @Transactional
    @Rollback
    public void findByNonExistentFirstLastTest() {
        assertFalse(participantRepository.findByFirstLast(FIRST, LAST).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void findByExistingEmailTest() {
        ParticipantEntity participantEntity = createParticipantEntity();
        compare(participantEntity, participantRepository.findByEmail(EMAIL).orElseThrow(RuntimeException::new));
    }

    @Test
    @Transactional
    @Rollback
    public void findByNonExistentEmailTest() {
        assertFalse(participantRepository.findByEmail(EMAIL).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void saveTest() {
        ParticipantResponse participantResponse = participantRepository.save(new ParticipantRequest(FIRST, LAST, EMAIL));
        compare(entityManager.find(ParticipantEntity.class, participantResponse.getId()), participantResponse);
    }

    @Test
    @Transactional
    @Rollback
    public void updateTest() {
        ParticipantEntity participantEntity = ParticipantEntity.builder()
                .firstName("Bill")
                .lastName("Gates")
                .email("bg@host.dmn")
                .build();

        entityManager.persist(participantEntity);

        ParticipantResponse participantResponse = participantRepository.update(
                participantEntity.participantId(),
                new ParticipantRequest(FIRST, LAST, EMAIL));

        assertEquals(FIRST, participantResponse.getFirstName());
        assertEquals(LAST, participantResponse.getLastName());
        assertEquals(EMAIL, participantResponse.getEmail());
        compare(participantEntity, participantResponse);

    }

    @Test
    @Transactional
    @Rollback
    public void getTest() {
        ParticipantEntity participantEntity = createParticipantEntity();

        compare(participantEntity, participantRepository.get(participantEntity.participantId()));
    }

    private ParticipantEntity createParticipantEntity() {
        ParticipantEntity participantEntity = ParticipantEntity.builder()
                .firstName(FIRST)
                .lastName(LAST)
                .email(EMAIL)
                .build();

        entityManager.persist(participantEntity);

        return participantEntity;
    }

    private void compare(ParticipantEntity entity, ParticipantResponse dto) {
        assertEquals(entity.firstName(), dto.getFirstName());
        assertEquals(entity.lastName(), dto.getLastName());
        assertEquals(entity.email(), dto.getEmail());
    }
}
