package com.assignment.backend.logic.authentication;

import com.assignment.backend.dtos.LoginRequest;
import com.assignment.backend.entities.CustomerEntity;
import com.assignment.backend.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final JwtUtil jwtUtil;
    private final AuthenticationService authenticationService;
    private final CacheManager cacheManager;

    public AuthenticationController(AuthenticationService authenticationService, JwtUtil jwtUtil, CacheManager cacheManager) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
        this.cacheManager = cacheManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        CustomerEntity authenticatedUser = authenticationService.authenticate(loginRequest);
        String jwtToken = jwtUtil.generateToken(authenticatedUser.getUsername());

        Cache energyPricesCache = cacheManager.getCache("energyPrices");

        if (energyPricesCache != null) {
            log.info("Clearing energyPrices cache");
            energyPricesCache.evict(authenticatedUser.getUsername());
        }

        return ResponseEntity.ok(
                new LoginResponse(
                        authenticatedUser.getUsername(),
                        authenticatedUser.getFirstName() + " " + authenticatedUser.getLastName(),
                        jwtToken
                )
        );
    }

    record LoginResponse(String username, String fullName, String token) {}
}
