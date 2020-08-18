package com.carbonclick.tsttask.secretsanta.participant;

import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.repository.ParticipantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParticipantTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    @Rollback
    public void listParticipantTest() throws Exception {

        participantRepository.save(ParticipantRequest.builder()
                .firstName("John")
                .lastName("Smith")
                .email("user@host.dmn")
                .build());

        mvc.perform( MockMvcRequestBuilders
                .get("/participant")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void getParticipantTest() throws Exception {

        ParticipantResponse participant = participantRepository.save(ParticipantRequest.builder()
                .firstName("John")
                .lastName("Smith")
                .email("user@host.dmn")
                .build());

        mvc.perform( MockMvcRequestBuilders
                .get("/participant/" + participant.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user@host.dmn"));
    }

    @Test
    @Transactional
    @Rollback
    public void updateParticipantTest() throws Exception {

        ParticipantResponse participant = participantRepository.save(ParticipantRequest.builder()
                .firstName("John")
                .lastName("Smith")
                .email("user@host.dmn")
                .build());

        String body = objectMapper.writeValueAsString(
                ParticipantRequest.builder()
                        .firstName("Bill")
                        .lastName("Gates")
                        .email("bill@host.dmn")
                        .build()
        );

        mvc.perform( MockMvcRequestBuilders
                .put("/participant/" + participant.getId())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Bill"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Gates"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("bill@host.dmn"));
    }

    @Test
    @Transactional
    @Rollback
    public void createParticipantTest() throws Exception {

        String body = objectMapper.writeValueAsString(
                ParticipantRequest.builder()
                        .firstName("John")
                        .lastName("Smith")
                        .email("user@host.dmn")
                        .build()
        );

        mvc.perform( MockMvcRequestBuilders
                .post("/participant")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user@host.dmn"));
    }
}
