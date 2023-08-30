package com.logicea.cardsapi.integration.rest.controller;

import com.logicea.cardsapi.AbstractIntegrationTest;
import com.logicea.cardsapi.AbstractIntegrationTestInitializer;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@ContextConfiguration(initializers = AbstractIntegrationTestInitializer.class)
class AuthenticationControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        super.afterEach();
        this.wireMockServer.resetAll();
    }

    @Test
    @DisplayName(value = "Given a Valid username and Password should generate a valid auth token")
    void testSuccessfulUserAuthentication() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/authenticate")
                        .content(createSuccessfulAdminAuthenticationRequestStub())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        log.info("Authentication response: {}", result.getResponse().getContentAsString());
        assertEquals(200, status);
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
        assertNotNull(response);
        assertNotNull(response.getString("token"));
    }
}