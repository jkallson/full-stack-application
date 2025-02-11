package com.assignment.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/users";
    }

    @Test
    @DisplayName("If user is not logged in -> FORBIDDEN")
    void test01() {
        ResponseEntity<String> registerResponse = restTemplate.getForEntity(baseUrl + "/consumption", String.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("If user has logged in return users consumption")
    void test02() {
        AuthenticationControllerIT.LoginRequest registerRequest = new AuthenticationControllerIT.LoginRequest("marimets", "marimets");
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/authentication/" + "/login", registerRequest, String.class);

        String jwtToken = "Bearer " + loginResponse.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/consumption",
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
