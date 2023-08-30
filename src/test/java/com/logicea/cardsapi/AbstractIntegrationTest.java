package com.logicea.cardsapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.logicea.cardsapi.rest.dto.request.LoginRequest;
import com.logicea.cardsapi.rest.facade.impl.AuthenticationFacadeImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.fail;

@RequiredArgsConstructor
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
@ContextConfiguration(initializers = AbstractIntegrationTestInitializer.class)
public abstract class AbstractIntegrationTest  {
    protected ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Autowired
    protected WireMockServer wireMockServer;

    @Autowired
    protected AuthenticationFacadeImpl authenticationFacade;

    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
    }

    protected String generateAdminUserTestToken() throws Exception {
        LoginRequest request = new LoginRequest();
        request = objectMapper.readValue(createSuccessfulAdminAuthenticationRequestStub(), LoginRequest.class);
        return this.authenticationFacade.createAuthentication(request).getToken();
    }

    protected String generateNormalUserTestToken() throws Exception {
        LoginRequest request = new LoginRequest();
        request = objectMapper.readValue(createSuccessfulMemberAuthenticationRequestStub(), LoginRequest.class);
        return this.authenticationFacade.createAuthentication(request).getToken();
    }

    protected String createSuccessfulAdminAuthenticationRequestStub() {
        String text = "{}";
        try {
            File resource = new ClassPathResource("test-cases/requests/successful-admin-authentication-test-request.json").getFile();
            text = new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException e) {
            fail("Failed to retrieve test data [createSuccessfulAdminAuthenticationRequestStub] message = " + e.getMessage());
        }
        return text;
    }

    protected String createSuccessfulMemberAuthenticationRequestStub() {
        String text = "{}";
        try {
            File resource = new ClassPathResource("test-cases/requests/successful-member-authentication-test-request.json").getFile();
            text = new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException e) {
            fail("Failed to retrieve test data [createSuccessfulAdminAuthenticationRequestStub] message = " + e.getMessage());
        }
        return text;
    }

    protected String createFailedAuthenticationRequestStub() {
        String text = "{}";
        try {
            File resource = new ClassPathResource("test-cases/requests/failed-authentication-test-request.json").getFile();
            text = new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException e) {
            fail("Failed to retrieve test data [createFailedAuthenticationRequestStub] message = " + e.getMessage());
        }
        return text;
    }

    protected String createSuccessfulCreateCardRequestStub() {
        String text = "{}";
        try {
            File resource = new ClassPathResource("test-cases/requests/valid-card-request.json").getFile();
            text = new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException e) {
            fail("Failed to retrieve test data [createSuccessfulCreateCardRequestStub] message = " + e.getMessage());
        }
        return text;
    }

    protected String createInvalidCreateCardRequestStub() {
        String text = "{}";
        try {
            File resource = new ClassPathResource("test-cases/requests/invalid-card-request.json").getFile();
            text = new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException e) {
            fail("Failed to retrieve test data [createInvalidCreateCardRequestStub] message = " + e.getMessage());
        }
        return text;
    }

    protected String createUpdateCardRequestStub() {
        String text = "{}";
        try {
            File resource = new ClassPathResource("test-cases/requests/update-card-request.json").getFile();
            text = new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException e) {
            fail("Failed to retrieve test data [createUpdateCardRequestStub] message = " + e.getMessage());
        }
        return text;
    }

    protected String createDeleteCardRequestStub(long id) {
        String text = "{}";
        try {
            File resource = new ClassPathResource("test-cases/requests/delete-card-request.json").getFile();
            text = new String(Files.readAllBytes(resource.toPath()))
                    .replace("#ID", String.valueOf(id));
        } catch (IOException e) {
            fail("Failed to retrieve test data [createUpdateCardRequestStub] message = " + e.getMessage());
        }
        return text;
    }
}
