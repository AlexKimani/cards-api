package com.logicea.cardsapi.integration.rest.controller;

import com.logicea.cardsapi.AbstractIntegrationTest;
import com.logicea.cardsapi.AbstractIntegrationTestInitializer;
import com.logicea.cardsapi.core.entity.Card;
import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.core.service.CardService;
import com.logicea.cardsapi.core.service.impl.CardServiceImpl;
import com.logicea.cardsapi.exception.CardNotFoundException;
import com.logicea.cardsapi.rest.dto.response.CardResponse;
import com.logicea.cardsapi.rest.facade.impl.CardFacadeImpl;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = AbstractIntegrationTestInitializer.class)
class CardControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CardServiceImpl cardService;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        super.afterEach();
        this.wireMockServer.resetAll();
    }

    @Test
    @Order(1)
    @DisplayName(value = "Given a valid Card request and token, should create a Card object")
    void testGivenAValidCardRequestAndTokenShouldCreateACardObject() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/card/create")
                        .content(createSuccessfulCreateCardRequestStub())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+this.generateAdminUserTestToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(201, status);
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(response);
        long id = response.getInt("id");
        Card cardResponse = this.cardService.getCardById(id).orElseThrow(() ->new CardNotFoundException(ErrorCode.ERROR_1604.getMessage()));
        assertEquals(cardResponse.getName(), response.getString("name"));
        assertEquals(cardResponse.getDescription(), response.getString("description"));
    }

    @Test
    @Order(2)
    @DisplayName(value = "Given a invalid Card request and token, should return a validation failed exception")
    void testGivenAnInValidCardRequestAndTokenShouldReturnAValidationFailedException() throws Exception {
        List<String> expectedErrors = List.of(
                "The name cannot be blank",
                "The color must be of color code format: eg: #FF3637"
        );
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/card/create")
                        .content(createInvalidCreateCardRequestStub())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+this.generateAdminUserTestToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(400, status);
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(response);
        Assertions.assertEquals(ErrorCode.ERROR_1400.getCode(), response.get("code"));
        JSONArray subErrors =  response.getJSONArray("sub_errors");
        for (int i = 0; i < subErrors.length(); i++) {
            JSONObject error = subErrors.getJSONObject(i);
            String message = error.getString("message");
            assertTrue(expectedErrors.stream().anyMatch(s -> s.equalsIgnoreCase(message)));
        }
    }

    @Test
    @Order(3)
    @DisplayName(value = "Given a Valid token, should return a paged and sorted list of cards")
    void testGivenAValidTokenShouldReturnAPagedAndSortedListOfCards() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/card/all")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+this.generateAdminUserTestToken())
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort_field", "color")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(response);
        assertEquals(5, response.getInt("size"));
    }

    @Test
    @Order(4)
    @DisplayName(value = "Given a Valid Admin token, should return a paged and sorted list of cards for Admin API")
    void testGivenAValidAdminTokenShouldReturnAPagedAndSortedListOfCards() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/card/admin")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+this.generateAdminUserTestToken())
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort_field", "color")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(response);
        assertEquals(5, response.getInt("size"));
    }

    @Test
    @Order(5)
    @DisplayName(value = "Given a Valid user token, should fail with authorization error")
    void testGivenAValidUserTokenShouldFailWithAuthorizationErrorForAdminURL() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/card/admin")
                        .content(createSuccessfulCreateCardRequestStub())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+this.generateNormalUserTestToken())
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort_field", "color")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(401, status);
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(response);
        assertEquals(ErrorCode.ERROR_1006.getCode(), response.getString("code"));
    }

    @Test
    @Order(6)
    @DisplayName(value = "Given a Valid user token and card id, should return a card object")
    void testGivenAValidUserTokenAndCardIdShouldReturnACardObject() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/card/id/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+this.generateAdminUserTestToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(response);
        assertEquals(1, response.getInt("id"));
    }

    @Test
    @Order(7)
    @DisplayName(value = "Given a valid card id and update request, should return a card response")
    void testGivenAValidCardIdAndUpdateRequestShouldReturnACardResponse() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/card/update/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+this.generateAdminUserTestToken())
                        .content(this.createUpdateCardRequestStub())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(response);
        assertEquals(1, response.getInt("id"));
        long id = response.getInt("id");
        Card cardResponse = this.cardService.getCardById(id).orElseThrow(() ->new CardNotFoundException(ErrorCode.ERROR_1604.getMessage()));
        assertEquals(cardResponse.getName(), response.getString("name"));
        assertEquals(cardResponse.getDescription(), response.getString("description"));
    }

    @Test
    @Order(8)
    @DisplayName(value = "Given a valid delete card request that belongs to a user, should delete the card")
    void testGivenAValidDeleteCardRequestThatBelongsToAUserShouldDeleteTheCard() throws Exception {
        long id = 1;
        String testDeleteIdMessage = String.format(ErrorCode.ERROR_1605.getMessage(), id);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/card/delete")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+this.generateAdminUserTestToken())
                        .content(this.createDeleteCardRequestStub(id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(response);
        assertEquals(testDeleteIdMessage, response.getString("message"));
        assertEquals(ErrorCode.ERROR_1605.getCode(), response.getString("code"));

    }

    @Test
    @Order(9)
    @DisplayName(value = "Given that a user is an admin, should be able to delete all cards")
    void testGivenThatAUserIsAnAdminShouldBeAbleToDeleteAllCards() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/card/admin/delete/all")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+this.generateAdminUserTestToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(response);
        assertEquals(ErrorCode.ERROR_1606.getMessage(), response.getString("message"));
        assertEquals(ErrorCode.ERROR_1606.getCode(), response.getString("code"));
    }

    @Test
    @Order(10)
    @DisplayName(value = "Given that a user is not an admin, should get Access Denied")
    void testGivenThatAUserIsNotAnAdminShouldGetAccessDenied() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/card/admin/delete/all")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+this.generateNormalUserTestToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(401, status);
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(response);
        assertEquals(ErrorCode.ERROR_1006.getMessage(), response.getString("message"));
        assertEquals(ErrorCode.ERROR_1006.getCode(), response.getString("status"));
    }
}