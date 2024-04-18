package com.example.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource("classpath:application-test.yaml")
class OrderApplicationTests {

    @Test
    void contextLoads() {

    }

}
