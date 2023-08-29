package com.logicea.cardsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.logicea.cardsapi.core.audit.AuditAwareImpl;
import com.logicea.cardsapi.core.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * The type Cards api application.
 */
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
@RequiredArgsConstructor
public class CardsApiApplication {
    private final ObjectMapper objectMapper;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        setup().run(args);
    }

    /**
     * Sets .
     *
     * @return the
     */
    public static SpringApplication setup() {
        SpringApplication springApplication = new SpringApplication(CardsApiApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        return springApplication;
    }

    /**
     * Post construct.
     */
    @PostConstruct
    public void postConstruct() {
        this.objectMapper
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule());
    }

    /**
     * Auditor aware bean.
     *
     * @return the auditor aware
     */
    @Bean
    public AuditorAware<User> auditorAware() {
        return new AuditAwareImpl();
    }

}
