package com.arjanvanraamsdonk.goodsnext.intergration;

import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.dtos.ShopInputDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShopControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllShops() throws Exception {
        mockMvc.perform(get("/api/shops")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }



    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetShopById() throws Exception {
        long shopId = 1;

        mockMvc.perform(get("/api/shops/{id}", shopId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shopId").value(shopId))
                .andExpect(jsonPath("$.shopName").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddShop() throws Exception {
        ShopInputDto shopInputDto = new ShopInputDto();
        shopInputDto.setShopName("New Shop");
        shopInputDto.setLogo(1L);
        shopInputDto.setContactInfo(new ContactInfoDto(
                "newshop@example.com",
                "City",
                "1234AB",
                "Street 1",
                "0612345678"
        ));

        mockMvc.perform(post("/api/shops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopInputDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shopName").value("New Shop"))
                .andExpect(jsonPath("$.contactInfo.email").value("newshop@example.com"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateShop() throws Exception {
        long shopId = 1;
        ShopInputDto shopInputDto = new ShopInputDto();
        shopInputDto.setShopName("Updated Shop");
        shopInputDto.setLogo(2L);
        shopInputDto.setContactInfo(new ContactInfoDto(
                "updatedshop@example.com",
                "New City",
                "5678CD",
                "New Street 123",
                "0612345679"
        ));

        mockMvc.perform(put("/api/shops/{id}", shopId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopInputDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shopName").value("Updated Shop"))
                .andExpect(jsonPath("$.contactInfo.email").value("updatedshop@example.com"));
    }



    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddShop_InvalidInput() throws Exception {
        ShopInputDto invalidShopInputDto = new ShopInputDto();

        mockMvc.perform(post("/api/shops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidShopInputDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.shopName").value("Shop name is mandatory"));
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/shops")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
