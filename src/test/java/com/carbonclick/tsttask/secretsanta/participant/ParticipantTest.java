package com.carbonclick.tsttask.secretsanta.participant;

import com.carbonclick.tsttask.secretsanta.BaseTest;
import com.carbonclick.tsttask.secretsanta.base.exceptionhandling.response.ErrorResponse;
import com.carbonclick.tsttask.secretsanta.participant.controller.request.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.repository.ParticipantRepository;
import com.carbonclick.tsttask.secretsanta.user.controller.request.LoginRequest;
import com.carbonclick.tsttask.secretsanta.user.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ParticipantTest extends BaseTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final String EMAIL = "user@host.dmn";

    @Autowired
    private ParticipantRepository participantRepository;

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
    public void listParticipantTest() throws Exception {

        participantRepository.save(ParticipantRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build());

        mvc.perform( MockMvcRequestBuilders
                .get("/participant")
                .header("Authorization", "Bearer " + authToken)
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
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build());

        mvc.perform( MockMvcRequestBuilders
                .get("/participant/" + participant.getId())
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(EMAIL));
    }

    @Test
    @Transactional
    @Rollback
    public void updateParticipantTest() throws Exception {

        ParticipantResponse participant = participantRepository.save(ParticipantRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
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
                .header("Authorization", "Bearer " + authToken)
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
                        .firstName(FIRST_NAME)
                        .lastName(LAST_NAME)
                        .email(EMAIL)
                        .build()
        );

        mvc.perform( MockMvcRequestBuilders
                .post("/participant")
                .header("Authorization", "Bearer " + authToken)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(EMAIL));
    }

    @Test
    @Transactional
    @Rollback
    public void tryCreateParticipantWithTheSameFullName() throws Exception {

        participantRepository.save(ParticipantRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build());

        String body = objectMapper.writeValueAsString(
                ParticipantRequest.builder()
                        .firstName(FIRST_NAME)
                        .lastName(LAST_NAME)
                        .email("another_email@host.dmn")
                        .build()
        );

        String response = mvc.perform( MockMvcRequestBuilders
                .post("/participant")
                .header("Authorization", "Bearer " + authToken)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(2, errorResponse.getFieldViolations().size());

        assertTrue(errorResponse.getFieldViolations().stream().anyMatch(t ->
                (t.getFieldName().equals("firstName"))
                && (t.getMessage().equals("First and last name should be unique"))
                && (t.getObjectName().equals("participantRequest"))));

        assertTrue(errorResponse.getFieldViolations().stream().anyMatch(t ->
                t.getFieldName().equals( "lastName")
                && t.getMessage().equals("First and last name should be unique")
                && t.getObjectName().equals("participantRequest")));
    }

    @Test
    @Transactional
    @Rollback
    public void tryCreateParticipantWithTheSameEmail() throws Exception {

        participantRepository.save(ParticipantRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build());

        String body = objectMapper.writeValueAsString(
                ParticipantRequest.builder()
                        .firstName("AnotherFirstName")
                        .lastName(LAST_NAME)
                        .email(EMAIL)
                        .build()
        );

        String response = mvc.perform( MockMvcRequestBuilders
                .post("/participant")
                .header("Authorization", "Bearer " + authToken)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(1, errorResponse.getFieldViolations().size());

        assertTrue(errorResponse.getFieldViolations().stream().anyMatch(t ->
                t.getFieldName().equals("email")
                && t.getMessage().equals("Email should be unique")
                && t.getObjectName().equals("participantRequest")));
    }
}
