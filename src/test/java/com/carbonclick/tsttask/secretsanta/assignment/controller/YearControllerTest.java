package com.carbonclick.tsttask.secretsanta.assignment.controller;

import com.carbonclick.tsttask.secretsanta.assignment.controller.request.NewAssignmentRequest;
import com.carbonclick.tsttask.secretsanta.assignment.controller.response.AssignmentResponse;
import com.carbonclick.tsttask.secretsanta.assignment.controller.response.YearResponse;
import com.carbonclick.tsttask.secretsanta.assignment.repository.AssignmentRepository;
import com.carbonclick.tsttask.secretsanta.assignment.repository.YearRepository;
import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.repository.ParticipantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private YearRepository yearRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Test
    @Transactional
    @Rollback
    public void assignmentTest() throws Exception {
        Set<Long> participantIds = createInitialParticipants(5);
        singleAssignmentTest(participantIds);
        singleAssignmentTest(participantIds);
    }

    private void singleAssignmentTest(Set<Long> participantIds) throws Exception {

        String body = objectMapper.writeValueAsString(
                NewAssignmentRequest.builder()
                        .title("Year 2020")
                        .participants(participantIds)
                        .build()
        );

        String result = mvc.perform( MockMvcRequestBuilders
                .post("/year/generate")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        YearResponseTest newYear = objectMapper.readValue(result, YearResponseTest.class);

        // TODO: Check year repo
        Page<YearResponse> page = yearRepository.list(new PageRequest(0, 10));
        assert page.getContent().get(0).getTitle().equals("Year 2020");

        mvc.perform( MockMvcRequestBuilders
                .get("/year")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());

        mvc.perform( MockMvcRequestBuilders
                .get("/year/" + newYear.getId())   // TODO: Get the number
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String resultAssignmentsString = mvc.perform( MockMvcRequestBuilders
                .get("/year/" + newYear.getId() + "/assignment")   // TODO: Get the number
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TestPageResponse<AssignmentResponse> resultAssignments = objectMapper.readValue(resultAssignmentsString, TestPageResponse.class);
        assertTrue(resultAssignments.getContent().stream().allMatch(a -> a.getTaker().getId() != a.getGiver().getId()));
        assertTrue(allElementsDistinct(resultAssignments.getContent().stream().map(a -> a.getGiver().getId()).collect(Collectors.toList())));
        assertTrue(allElementsDistinct(resultAssignments.getContent().stream().map(a -> a.getTaker().getId()).collect(Collectors.toList())));
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
