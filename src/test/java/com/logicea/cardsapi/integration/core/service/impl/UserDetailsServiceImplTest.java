package com.logicea.cardsapi.integration.core.service.impl;

import com.logicea.cardsapi.AbstractIntegrationTest;
import com.logicea.cardsapi.core.repository.UserRepository;
import com.logicea.cardsapi.core.service.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class UserDetailsServiceImplTest extends AbstractIntegrationTest {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadUserByUserName() {
        log.info("{}",this.userDetailsService.loadUserByUserName("admin@test.com"));
    }

    @Test
    void checkListOfCards() {
        log.info("{}", this.userRepository.findUserByEmail("admin@test.com").get().getCards());
    }
}