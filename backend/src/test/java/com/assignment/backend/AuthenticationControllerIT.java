package com.assignment.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/authentication";
    }

    @Test
    @DisplayName("If wrong login info is given -> HttpStatus.FORBIDDEN")
    void test01() {
        LoginRequest registerRequest = new LoginRequest("hello", "world");
        ResponseEntity<String> registerResponse = restTemplate.postForEntity(
                baseUrl + "/login", registerRequest, String.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("If correct login information is used -> HttpStatus.FORBIDDEN")
    void test02() {
        LoginRequest registerRequest = new LoginRequest("marimets", "marimets");
        ResponseEntity<String> registerResponse = restTemplate.postForEntity(
                baseUrl + "/login", registerRequest, String.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    record LoginRequest(String username, String password) {}
}
