package com.ibizabroker.lms.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void testGenerateAndValidateToken() {
        // Set up
        // ⭐ SỬA LẠI: Dùng setters thay vì truy cập trực tiếp
        jwtUtil.setSecret("my-very-secret-key-that-is-at-least-32-bytes-long");
        jwtUtil.setExpirationMs(3600000); // 1 hour

        UserDetails user = User.withUsername("testuser").password("password").roles("USER").build();
        Map<String, Object> extraClaims = Map.of("userId", 123);

        // Generate token
        String token = jwtUtil.generateToken(user, extraClaims);
        assertNotNull(token);

        // Validate token
        assertTrue(jwtUtil.validateToken(token, user));
        assertEquals("testuser", jwtUtil.getUsernameFromToken(token));
    }

    @Test
    void testExpiredToken() throws InterruptedException {
        // Set up
        // ⭐ SỬA LẠI: Dùng setters thay vì truy cập trực tiếp
        jwtUtil.setSecret("my-very-secret-key-that-is-at-least-32-bytes-long");
        jwtUtil.setExpirationMs(1); // 1 millisecond

        UserDetails user = User.withUsername("testuser").password("password").roles("USER").build();
        String token = jwtUtil.generateToken(user, null);

        // Wait for token to expire
        Thread.sleep(10);

        // Validate token
        assertFalse(jwtUtil.validateToken(token, user));
    }
}