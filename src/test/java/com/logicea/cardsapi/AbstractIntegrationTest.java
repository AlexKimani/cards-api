package com.logicea.cardsapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
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



    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
    }

    protected String createSuccessfulAuthenticationRequestStub() {
        String text = "{}";
        try {
            File resource = new ClassPathResource("test-cases/requests/successful-authentication-test-request.json").getFile();
            text = new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException e) {
            fail("Failed to retrieve test data [createSuccessfulAuthenticationRequestStub] message = " + e.getMessage());
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

}
