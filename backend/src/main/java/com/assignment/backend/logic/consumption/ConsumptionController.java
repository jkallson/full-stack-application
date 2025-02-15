package com.assignment.backend.logic.consumption;

import com.assignment.backend.logic.consumption.ConsumptionService.MeteringPointConsumption;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/consumption")
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @GetMapping("/metering-points")
    public List<MeteringPointConsumption> meteringPointConsumptions (Principal principal) {
        return consumptionService.getMeteringPointsConsumption(principal.getName());
    }
}
