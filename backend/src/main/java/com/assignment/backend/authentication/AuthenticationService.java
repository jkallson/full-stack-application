package com.assignment.backend.authentication;

import com.assignment.backend.dtos.LoginRequest;
import com.assignment.backend.entities.CustomerEntity;
import com.assignment.backend.repositories.CustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            CustomerRepository customerRepository,
            AuthenticationManager authenticationManager
    ) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
    }

    public CustomerEntity authenticate(LoginRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.username(),
                        input.password()
                )
        );

        return customerRepository.findByUsername(input.username())
                .orElseThrow();
    }
}
