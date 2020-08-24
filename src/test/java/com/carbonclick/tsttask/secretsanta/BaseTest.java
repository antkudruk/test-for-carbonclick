package com.carbonclick.tsttask.secretsanta;

import com.carbonclick.tsttask.secretsanta.assignment.controller.YearControllerTest;
import com.carbonclick.tsttask.secretsanta.user.controller.request.LoginRequest;
import com.carbonclick.tsttask.secretsanta.user.repository.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {
    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected EntityManager entityManager;

    protected String authToken;

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

        authToken = objectMapper.readValue(response, YearControllerTest.TestLoginResponse.class).token;
    }

    public static class TestLoginResponse {
        public String token;
    }
}
