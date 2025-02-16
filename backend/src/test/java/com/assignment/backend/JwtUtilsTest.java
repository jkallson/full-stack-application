package com.assignment.backend;

import com.assignment.backend.security.JwtProperties;
import com.assignment.backend.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil(new JwtProperties("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", 3600000));
    }

    @Test
    @DisplayName("Can generate JWT token")
    void test01() {
        String token = jwtUtil.generateToken("testUser");
        assertNotNull(token);
    }

    @Test
    @DisplayName("Can extract correct username from generated JWT")
    void test02() {
        String token = jwtUtil.generateToken("testUser");
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals("testUser", extractedUsername);
    }

    @Test
    @DisplayName("Marks token as valid when given valid token")
    void test03() {
        String token = jwtUtil.generateToken("testUser");
        assertTrue(jwtUtil.validateToken(token, "testUser"));
    }

    @Test
    @DisplayName("When token expired validate that ExpiredJwtException is thrown")
    void test04() throws InterruptedException {
        JwtUtil shortLivedJwt = new JwtUtil(new JwtProperties("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", 1));
        String token = shortLivedJwt.generateToken("testUser");

        Thread.sleep(10);
        Exception exception = assertThrows(ExpiredJwtException.class, () -> jwtUtil.validateToken(token, "testUser"));

        assertTrue(exception.getMessage().contains("JWT expired"));
    }
}
