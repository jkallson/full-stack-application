package com.assignment.backend.logic.consumption;

import com.assignment.backend.dtos.ConsumptionDto;
import com.assignment.backend.dtos.EnergyPriceDto;
import com.assignment.backend.entities.ConsumptionEntity;
import com.assignment.backend.entities.CustomerEntity;
import com.assignment.backend.entities.MeteringPointEntity;
import com.assignment.backend.logic.EnergyPriceService;
import com.assignment.backend.repositories.CustomerRepository;
import com.assignment.backend.repositories.MeteringPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConsumptionService {
    private static final Logger log = LoggerFactory.getLogger(ConsumptionService.class);

    private final MeteringPointRepository meteringPointRepository;
    private final CustomerRepository customerRepository;
    private final EnergyPriceService energyPriceService;

    public ConsumptionService(MeteringPointRepository meteringPointRepository, CustomerRepository customerRepository, EnergyPriceService energyPriceService) {
        this.meteringPointRepository = meteringPointRepository;
        this.energyPriceService = energyPriceService;
        this.customerRepository = customerRepository;
    }

    public List<MeteringPointConsumption> getMeteringPointsConsumption(String username) {
        CustomerEntity customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found"));

        List<MeteringPointEntity> meteringPoints = meteringPointRepository.findAllByCustomer(customer);
        List<EnergyPriceDto> energyPrices = energyPriceService.getEnergyPrices(username);

        return meteringPoints.stream()
                .map(meteringPoint -> new MeteringPointConsumption(
                        meteringPoint.getAddress(),
                        findMonthlyConsumption(meteringPoint, energyPrices)
                ))
                .toList();
    }

    private List<MonthlyConsumption> findMonthlyConsumption(MeteringPointEntity meteringPoint, List<EnergyPriceDto> prices) {
        Map<YearMonth, List<ConsumptionEntity>> grouped = meteringPoint.getConsumptions().stream()
                .collect(Collectors.groupingBy(cons -> YearMonth.from(cons.getConsumptionTime())));

        return grouped.entrySet().stream()
                .map(entry -> {
                    YearMonth month = entry.getKey();
                    List<ConsumptionDto> consumptions = entry.getValue()
                            .stream()
                            .map(ConsumptionEntity::toDto)
                            .toList();

                    double totalConsumption = consumptions.stream()
                            .mapToDouble(ConsumptionDto::amount)
                            .sum();

                    ZonedDateTime consumptionTime = consumptions.getFirst().time();
                    EnergyPriceDto energyPrice = prices.stream()
                            .filter(price -> price.includes(consumptionTime))
                            .findFirst()
                            .orElseGet(() -> {
                                log.error("Could not find energy price for date: {}", consumptionTime);
                                return null;
                            });

                    return new MonthlyConsumption(
                            month.getYear(),
                            month.getMonthValue(),
                            totalConsumption,
                            costPerMonth(energyPrice, totalConsumption),
                            energyPrice,
                            consumptions
                    );
                })
                .toList();
    }

    private MonthlyConsumptionCost costPerMonth(EnergyPriceDto energyPrice, Double totalConsumption) {
        if (energyPrice == null) {
            return new MonthlyConsumptionCost(0.0, 0.0);
        }

        double costPerKwh = totalConsumption * (energyPrice.centsPerKwh() / 100);
        double costPerKwhWithVat = totalConsumption * (energyPrice.centsPerKwhWithVat() / 100);

        return new MonthlyConsumptionCost(
                (double) Math.round(costPerKwh * 100) / 100,
                (double) Math.round(costPerKwhWithVat * 100) / 100
        );
    }

    public record MeteringPointConsumption(String address, List<MonthlyConsumption> consumptions) { }

    public record MonthlyConsumption(
            Integer year,
            Integer month,
            Double totalConsumption,
            MonthlyConsumptionCost consumptionCost,
            EnergyPriceDto energyPrice,
            List<ConsumptionDto> entries) { }

    public record MonthlyConsumptionCost(
            Double costPerKwh,
            Double costPerKwhWithVat
    ) {}

}
