package com.arjanvanraamsdonk.goodsnext.intergration;


import com.arjanvanraamsdonk.goodsnext.dtos.UserInputDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(jsonPath("$.roles").isArray());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddUser() throws Exception {
        UserInputDto validUserInputDto = new UserInputDto();
        validUserInputDto.setUsername("testuser");
        validUserInputDto.setPassword("Test1234!");
        validUserInputDto.setAuthorities(Collections.singletonList("ROLE_USER"));



        this.mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserInputDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddUser_InvalidInput() throws Exception {
        UserInputDto invalidUserInputDto = new UserInputDto();
        invalidUserInputDto.setUsername("");
        invalidUserInputDto.setPassword("");
        invalidUserInputDto.setAuthorities(Collections.emptyList());


        this.mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserInputDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("Username is mandatory"))
                .andExpect(jsonPath("$.password").value("Password is mandatory"))
                .andExpect(jsonPath("$.authorities").value("At least one authority is required"));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUser_ValidInput() throws Exception {
        String validUserJson = """
                {
                    "username": "updateduser",
                    "password": "newpassword",
                    "contactInfo": {
                        "email": "updateduser@example.com",
                        "city": "UpdatedCity",
                        "postalCode": "5678CD",
                        "address": "Updated Street 123",
                        "phoneNumber": "0623456789"
                    },
                    "authorities": ["ROLE_USER"]
                }
                """;

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUserJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
