package com.carbonclick.tsttask.secretsanta.user;

import com.carbonclick.tsttask.secretsanta.user.controller.request.LoginRequest;
import com.carbonclick.tsttask.secretsanta.user.repository.entity.UserEntity;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    @Rollback
    public void test() throws Exception {
        // insert into user (username, password) values ('admin', '$2a$10$aDipcD0hx5janQqNYMqKBe.fBx6TSY8Kvyu4zfyQ0oU4/qs4TK/1O');
        UserEntity user = UserEntity.builder()
                .username("admin")
                .password("$2a$10$aDipcD0hx5janQqNYMqKBe.fBx6TSY8Kvyu4zfyQ0oU4/qs4TK/1O")   // 123456
                .build();

        entityManager.persist(user);

        LoginRequest req = new LoginRequest("admin", "123456");

        mvc.perform( MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
