package com.logicea.cardsapi;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CardsApiApplicationTests extends AbstractIntegrationTest {

    @Test
    void contextLoads(ApplicationContext context) {
        assertNotNull(context, "Application Context is Null");
        val app = CardsApiApplication.setup();
        val listeners = app.getListeners();
        val applicationPidFileWriterListeners = listeners.stream()
                .filter(listener -> listener instanceof ApplicationPidFileWriter);

        // then
        assertEquals(1, applicationPidFileWriterListeners.count());
    }
}
