package com.carbonclick.tsttask.secretsanta.assignment.controller;

import com.carbonclick.tsttask.secretsanta.assignment.controller.request.NewAssignmentRequest;
import com.carbonclick.tsttask.secretsanta.assignment.controller.response.AssignmentResponse;
import com.carbonclick.tsttask.secretsanta.assignment.controller.response.YearResponse;
import com.carbonclick.tsttask.secretsanta.assignment.repository.YearRepository;
import com.carbonclick.tsttask.secretsanta.assignment.repository.entity.YearEntity;
import com.carbonclick.tsttask.secretsanta.base.exceptionhandling.response.ErrorResponse;
import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.request.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.repository.ParticipantRepository;
import com.carbonclick.tsttask.secretsanta.user.controller.request.LoginRequest;
import com.carbonclick.tsttask.secretsanta.user.repository.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class YearControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private YearRepository yearRepository;

    private String authToken;

    public static class TestLoginResponse {
        public String token;
    }

    @BeforeEach
    public void initJwtToken() throws Exception {
        UserEntity user = UserEntity.builder()
                .username("admin")
                .password("$2a$10$aDipcD0hx5janQqNYMqKBe.fBx6TSY8Kvyu4zfyQ0oU4/qs4TK/1O")   // 123456
                .build();

        entityManager.persist(user);

        LoginRequest req = new LoginRequest("admin", "123456");

        String response = mvc.perform( MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        authToken = objectMapper.readValue(response, TestLoginResponse.class).token;
    }

    @Test
    @Transactional
    @Rollback
    public void assignmentTest() throws Exception {
        Set<Long> participantIds = createInitialParticipants(5);
        singleAssignmentTest("Year 2020", participantIds);
        singleAssignmentTest("Year 2019", participantIds);
    }

    private void singleAssignmentTest(String yearName, Set<Long> participantIds) throws Exception {

        String body = objectMapper.writeValueAsString(
                NewAssignmentRequest.builder()
                        .title(yearName)
                        .participants(participantIds)
                        .build()
        );

        String result = mvc.perform( MockMvcRequestBuilders
                .post("/year/generate")
                .header("Authorization", "Bearer " + authToken)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TestYearResponse newYear = objectMapper.readValue(result, TestYearResponse.class);

        Page<YearResponse> page = yearRepository.list(new PageRequest(0, 10));
        assert page.getContent().get(0).getTitle().equals(yearName);

        mvc.perform( MockMvcRequestBuilders
                .get("/year")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());

        mvc.perform( MockMvcRequestBuilders
                .get("/year/" + newYear.getId())
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String resultAssignmentsString = mvc.perform( MockMvcRequestBuilders
                .get("/year/" + newYear.getId() + "/assignment")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<AssignmentResponse> resultAssignments = (List<AssignmentResponse>)objectMapper.readValue(resultAssignmentsString, TestPageResponse.class)
                .getContent()
                .stream()
                .map(map -> objectMapper.convertValue(map, AssignmentResponse.class))
                .collect(Collectors.toList());

        resultAssignments.stream().allMatch(a -> a.getTaker().getId() != a.getGiver().getId());
        assertTrue(allElementsDistinct(resultAssignments.stream().map(a -> a.getGiver().getId()).collect(Collectors.toList())));
        assertTrue(allElementsDistinct(resultAssignments.stream().map(a -> a.getTaker().getId()).collect(Collectors.toList())));
    }

    @Test
    @Transactional
    @Rollback
    public void attemptToCreateYearWithTheSameName() throws Exception {
        entityManager.persist(YearEntity.builder()
                .title("Year 2020")
                .build());

        String body = objectMapper.writeValueAsString(
                NewAssignmentRequest.builder()
                        .title("Year 2020")
                        .participants(createInitialParticipants(5))
                        .build()
        );

        String result = mvc.perform( MockMvcRequestBuilders
                .post("/year/generate")
                .header("Authorization", "Bearer " + authToken)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(result, ErrorResponse.class);

        assertEquals(1, errorResponse.getFieldViolations().size());
        assertEquals("title", errorResponse.getFieldViolations().get(0).getFieldName());
        assertEquals("newAssignmentRequest", errorResponse.getFieldViolations().get(0).getObjectName());
        assertEquals("Year name should be unique", errorResponse.getFieldViolations().get(0).getMessage());
    }

    @Test
    @Transactional
    @Rollback
    public void attemptToCreateAssignmentWithOneParticipant() throws Exception {
        String body = objectMapper.writeValueAsString(
                NewAssignmentRequest.builder()
                        .title("Year 2020")
                        .participants(createInitialParticipants(1))
                        .build()
        );

        String result = mvc.perform( MockMvcRequestBuilders
                .post("/year/generate")
                .header("Authorization", "Bearer " + authToken)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(result, ErrorResponse.class);

        assertEquals(1, errorResponse.getFieldViolations().size());
        assertEquals("participants", errorResponse.getFieldViolations().get(0).getFieldName());
        assertEquals("newAssignmentRequest", errorResponse.getFieldViolations().get(0).getObjectName());
        assertEquals("Participants list should contain at least two participants",
                errorResponse.getFieldViolations().get(0).getMessage());
    }

    @Test
    @Transactional
    @Rollback
    public void attemptToCreateAssignmentWithEmptyParticipantList() throws Exception {
        String body = objectMapper.writeValueAsString(
                NewAssignmentRequest.builder()
                        .title("Year 2020")
                        .participants(Collections.emptySet())
                        .build()
        );

        String result = mvc.perform( MockMvcRequestBuilders
                .post("/year/generate")
                .header("Authorization", "Bearer " + authToken)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(result, ErrorResponse.class);

        assertEquals(1, errorResponse.getFieldViolations().size());
        assertEquals("participants", errorResponse.getFieldViolations().get(0).getFieldName());
        assertEquals("newAssignmentRequest", errorResponse.getFieldViolations().get(0).getObjectName());
        assertEquals("Participants list should contain at least two participants",
                errorResponse.getFieldViolations().get(0).getMessage());
    }


    private <T> boolean allElementsDistinct(List<T> elements) {
        Set<T> found = new HashSet<>();
        for(T element : elements) {
            if(found.contains(element)) {
                return false;
            }
            found.add(element);
        }
        return true;
    }

    private Set<Long> createInitialParticipants(int n) {
        return IntStream.range(0, n).boxed()
                .map(i -> participantRepository.save(ParticipantRequest.builder()
                        .firstName("First" + i)
                        .lastName("Last" + i)
                        .email("email" + i + "@gmail.com")
                        .build()))
                .map(ParticipantResponse::getId)
                .collect(Collectors.toSet());
    }
}
