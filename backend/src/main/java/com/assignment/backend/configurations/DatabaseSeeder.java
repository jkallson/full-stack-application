package com.assignment.backend.configurations;

import com.assignment.backend.entities.ConsumptionEntity;
import com.assignment.backend.entities.CustomerEntity;
import com.assignment.backend.entities.MeteringPointEntity;
import com.assignment.backend.repositories.ConsumptionRepository;
import com.assignment.backend.repositories.CustomerRepository;
import com.assignment.backend.repositories.MeteringPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.ZonedDateTime;
import java.util.List;

@Configuration
public class DatabaseSeeder {
    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Bean
    CommandLineRunner initDatabase(
            CustomerRepository customerRepository,
            MeteringPointRepository meteringPointRepository,
            ConsumptionRepository consumptionRepository
    ) {
        return args -> {
            if (customerRepository.count() == 0) {
                log.info("Seeding database with mock data...");

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

                CustomerEntity mari = new CustomerEntity("Mari", "Mets", "marimets", passwordEncoder.encode("marimets"));
                CustomerEntity madis = new CustomerEntity("Madis", "Mets", "madismets", passwordEncoder.encode("madismets"));

                customerRepository.saveAll(List.of(mari, madis));

                MeteringPointEntity meteringPoint1 = new MeteringPointEntity("Tartu mnt 123", mari);
                MeteringPointEntity meteringPoint2 = new MeteringPointEntity("Tallinna mnt 123", mari);
                MeteringPointEntity meteringPoint3 = new MeteringPointEntity("Pärnu mnt 123", madis);

                meteringPointRepository.saveAll(List.of(meteringPoint1, meteringPoint2, meteringPoint3));

                ZonedDateTime lastYear = ZonedDateTime.now().minusYears(1);

                List<ConsumptionEntity> consumptions = List.of(
                        new ConsumptionEntity(50, "kWh", lastYear, meteringPoint1),
                        new ConsumptionEntity(30, "kWh", lastYear, meteringPoint2),
                        new ConsumptionEntity(35, "kWh", lastYear.plusMonths(1), meteringPoint2),
                        new ConsumptionEntity(55, "kWh", lastYear.plusMonths(2), meteringPoint2),
                        new ConsumptionEntity(12, "kWh", lastYear.plusMonths(3), meteringPoint2),
                        new ConsumptionEntity(70, "kWh", lastYear.plusMonths(2), meteringPoint3),
                        new ConsumptionEntity(90, "kWh", lastYear.plusMonths(5), meteringPoint3)
                );

                consumptionRepository.saveAll(consumptions);

                log.info("Database seeded successfully.");
            } else {
                log.info("Database already contains data. Skipping seeding.");
            }
        };
    }
}
