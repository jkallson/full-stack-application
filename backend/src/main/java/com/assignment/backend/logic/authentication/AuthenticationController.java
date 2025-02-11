package com.assignment.backend.logic.authentication;

import com.assignment.backend.dtos.LoginRequest;
import com.assignment.backend.entities.CustomerEntity;
import com.assignment.backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    private final JwtUtil jwtUtil;
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        CustomerEntity authenticatedUser = authenticationService.authenticate(loginRequest);
        String jwtToken = jwtUtil.generateToken(authenticatedUser.getUsername());

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
