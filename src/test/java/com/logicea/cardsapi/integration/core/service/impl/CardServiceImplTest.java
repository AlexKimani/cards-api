package com.logicea.cardsapi.integration.core.service.impl;

import com.logicea.cardsapi.AbstractIntegrationTest;
import com.logicea.cardsapi.core.entity.User;
import com.logicea.cardsapi.core.repository.UserRepository;
import com.logicea.cardsapi.core.service.CardService;
import com.logicea.cardsapi.core.service.impl.CardServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CardServiceImplTest extends AbstractIntegrationTest {
    @Autowired
    private CardServiceImpl cardService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        this.user = this.userRepository.findUserByEmail("admin@test.com").orElse(null);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCardsByUser() {
        log.info("{}");
    }

    @Test
    void testGetAllCardsCreatedByUser()  throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("color"));
        log.info("{}", this.cardService.getAllCardsCreatedByUser("admin@test.com",pageable)
                .getContent());
    }
}