package com.assignment.backend.logic.users;

import com.assignment.backend.entities.CustomerEntity;
import com.assignment.backend.entities.MeteringPointEntity;
import com.assignment.backend.repositories.CustomerRepository;
import com.assignment.backend.repositories.MeteringPointRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final MeteringPointRepository meteringPointRepository;
    private final CustomerRepository customerRepository;

    public UserService(MeteringPointRepository meteringPointRepository, CustomerRepository customerRepository) {
        this.meteringPointRepository = meteringPointRepository;
        this.customerRepository = customerRepository;
    }

    public List<MeteringPointConsumption> getMeteringPointsConsumption(String username) {
        Optional<CustomerEntity> customer = customerRepository.findByUsername(username);

        if (customer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found");
        }

        // TODO: some kind of calculation
        List<MeteringPointEntity> meteringPoints = meteringPointRepository.findAllByCustomer(customer.get());

        return meteringPoints.stream()
                .map(meteringPoint -> new MeteringPointConsumption(
                        meteringPoint.getAddress(),
                        meteringPoint.getConsumptions().stream().map(consumption -> new Consumption(
                                consumption.getAmount(),
                                consumption.getAmountUnit(),
                                consumption.getConsumptionTime()
                        )).toList()
                )).toList();
    }



    public record MeteringPointConsumption(
            String address,
            List<Consumption> consumptions
    ) {}
    public record Consumption(
            Integer amount,
            String unit,
            ZonedDateTime date
    ) { }

}
