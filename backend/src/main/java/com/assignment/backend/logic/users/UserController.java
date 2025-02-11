package com.assignment.backend.logic.users;

import com.assignment.backend.logic.users.UserService.MeteringPointConsumption;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/consumption")
    public List<MeteringPointConsumption> meteringPointConsumptions (Principal principal) {
        return userService.getMeteringPointsConsumption(principal.getName());
    }
}
