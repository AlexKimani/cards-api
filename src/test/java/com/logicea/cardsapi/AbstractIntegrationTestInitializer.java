package com.logicea.cardsapi;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.format;

@Slf4j
public class AbstractIntegrationTestInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Integer WIRE_MOCK_PORT = 8111;
    private static final String TEST_DATABASE_NAME = "test_database";
    private static final String TEST_DATABASE_USERNAME = "test";
    private static final String TEST_DATABASE_PASSWORD = "test";
    public static final Integer MAX_ATTEMPTS = 5;

    private static final Network network = Network.newNetwork();

    protected static final DockerImageName MYSQL_DOCKER_IMAGE = DockerImageName.parse("mysql:latest");
    protected static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(MYSQL_DOCKER_IMAGE)
            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
            .withDatabaseName(TEST_DATABASE_NAME)
            .withUsername(TEST_DATABASE_USERNAME)
            .withPassword(TEST_DATABASE_PASSWORD)
            .withNetwork(network);

    public static final WireMockServer wireMockServer =
            new WireMockServer(new WireMockConfiguration().port(WIRE_MOCK_PORT));

    protected void startContainers() {
        Startables.deepStart(Stream.of(mySQLContainer)).join();
    }

    private void startWireMock() {
        wireMockServer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        startContainers();
        startWireMock();
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        applicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);

        applicationContext.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                wireMockServer.stop();
            }
        });

        environment.getPropertySources()
                .addFirst(new MapPropertySource("application", Map.of(
                        "spring.r2dbc.url", mySQLContainer.getJdbcUrl().replaceFirst("jdbc", "r2dbc"),
                        "spring.r2dbc.username", mySQLContainer.getUsername(),
                        "spring.r2dbc.password", mySQLContainer.getPassword()
                )));
    }

    @DynamicPropertySource
    private static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        // R2DBC DataSource Example
        registry.add("spring.r2dbc.url", () ->
                format("r2dbc:tc:mariadb://%s:%d/%s",
                        mySQLContainer.getHost(),
                        mySQLContainer.getFirstMappedPort(),
                        mySQLContainer.getDatabaseName()));
        registry.add("spring.r2dbc.username", mySQLContainer::getUsername);
        registry.add("spring.r2dbc.password", mySQLContainer::getPassword);
        registry.add("spring.liquibase.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.liquibase.user", mySQLContainer::getUsername);
        registry.add("spring.liquibase.password", mySQLContainer::getPassword);
    }
}

