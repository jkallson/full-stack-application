package com.assignment.backend.logic.users;

import com.assignment.backend.dtos.ConsumptionDto;
import com.assignment.backend.dtos.EnergyPrice;
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
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final MeteringPointRepository meteringPointRepository;
    private final CustomerRepository customerRepository;
    private final EnergyPriceService energyPriceService;

    public UserService(MeteringPointRepository meteringPointRepository, CustomerRepository customerRepository, EnergyPriceService energyPriceService) {
        this.meteringPointRepository = meteringPointRepository;
        this.energyPriceService = energyPriceService;
        this.customerRepository = customerRepository;
    }

    public List<MeteringPointConsumption> getMeteringPointsConsumption(String username) {
        CustomerEntity customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found"));

        List<MeteringPointEntity> meteringPoints = meteringPointRepository.findAllByCustomer(customer);
        List<EnergyPrice> energyPrices = energyPriceService.getEnergyPrices();

        return meteringPoints.stream()
                .map(meteringPoint -> new MeteringPointConsumption(
                        meteringPoint.getAddress(),
                        findMonthlyConsumption(meteringPoint, energyPrices)
                ))
                .toList();
    }

    private List<MonthlyConsumption> findMonthlyConsumption(MeteringPointEntity meteringPoint, List<EnergyPrice> prices) {
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
                    EnergyPrice energyPrice = prices.stream()
                            .filter(price -> price.includes(consumptionTime))
                            .findFirst()
                            .orElseGet(() -> {
                                log.error("Could not find energy price for date: {}", consumptionTime);
                                return null;
                            });

                    return new MonthlyConsumption(month.getYear(), month.getMonthValue(), totalConsumption, energyPrice, consumptions);
                })
                .toList();
    }

    public record MeteringPointConsumption(String address, List<MonthlyConsumption> consumptions) { }

    public record MonthlyConsumption(Integer year, Integer month, Double totalConsumption, EnergyPrice energyPrice, List<ConsumptionDto> entries) { }

}
