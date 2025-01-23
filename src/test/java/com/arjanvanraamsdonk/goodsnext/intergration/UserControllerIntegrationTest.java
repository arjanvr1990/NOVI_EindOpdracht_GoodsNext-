package com.arjanvanraamsdonk.goodsnext.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetUserById() throws Exception {
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
    public void testUpdateUser_ValidInput() throws Exception {
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





}
