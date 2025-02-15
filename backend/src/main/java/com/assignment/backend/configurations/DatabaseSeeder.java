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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

                List<ConsumptionEntity> consumptions = new ArrayList<>();

                for (int i = 0; i < 12; i++) {
                    ZonedDateTime time = createDateTimeFrom(2024, i + 1, 12);
                    consumptions.add(new ConsumptionEntity(generateAmount(), "kWh", time, meteringPoint1));
                    consumptions.add(new ConsumptionEntity(generateAmount(), "kWh", time, meteringPoint2));
                    consumptions.add(new ConsumptionEntity(generateAmount(), "kWh", time, meteringPoint3));
                }

                consumptions.add(new ConsumptionEntity(generateAmount(), "kWh", createDateTimeFrom(2024, 1, 16), meteringPoint1));

                consumptionRepository.saveAll(consumptions);

                log.info("Database seeded successfully.");
            } else {
                log.info("Database already contains data. Skipping seeding.");
            }
        };
    }

    private Integer generateAmount() {
        return ThreadLocalRandom.current().nextInt(150, 701);
    }

    private ZonedDateTime createDateTimeFrom(Integer year, Integer month, Integer day) {
        return ZonedDateTime.of(year, month, month, day, 0, 0, 0, ZoneId.systemDefault());
    }
}
