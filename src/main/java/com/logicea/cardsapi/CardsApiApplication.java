package com.logicea.cardsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
@RequiredArgsConstructor
public class CardsApiApplication {
    private final ObjectMapper objectMapper;

    public static void main(String[] args) {
        setup().run(args);
    }

    public static SpringApplication setup() {
        SpringApplication springApplication = new SpringApplication(CardsApiApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        return springApplication;
    }

    @PostConstruct
    public void postConstruct() {
        this.objectMapper
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule());
    }

}
