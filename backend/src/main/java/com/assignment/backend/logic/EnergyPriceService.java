package com.assignment.backend.logic;

import com.assignment.backend.dtos.EnergyPriceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
public class EnergyPriceService {
    private static final Logger log = LoggerFactory.getLogger(EnergyPriceService.class);
    private static final String API_URL = "https://estfeed.elering.ee/api/public/v1/energy-price/electricity";
    private final RestTemplate restTemplate;

    public EnergyPriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "energyPrices", key = "#username")
    public List<EnergyPriceDto> getEnergyPrices (String username) {
        log.info("Fetching energy prices over API for {}", username);
        String urlTemplate = UriComponentsBuilder.fromUriString(API_URL)
                .queryParam("startDateTime", "2024-01-01T00:00:00.000Z")
                .queryParam("endDateTime", "2024-12-31T23:59:59.999Z")
                .queryParam("resolution", "one_month")
                .encode()
                .toUriString();

        EnergyPriceDto[] response = restTemplate.getForObject(urlTemplate, EnergyPriceDto[].class);

        if (response == null) {
            log.warn("No results for energy prices fetch");
            return List.of();
        }

        return Arrays.asList(response);
    }
}
