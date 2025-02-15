package com.assignment.backend.logic;

import com.assignment.backend.dtos.EnergyPrice;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
public class EnergyPriceService {
    private final String API_URL = "https://estfeed.elering.ee/api/public/v1/energy-price/electricity";
    private final RestTemplate restTemplate;

    public EnergyPriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<EnergyPrice> getEnergyPrices () {
        String urlTemplate = UriComponentsBuilder.fromUriString(API_URL)
                .queryParam("startDateTime", "2024-01-01T00:00:00.000Z")
                .queryParam("endDateTime", "2024-12-31T23:59:59.999Z")
                .queryParam("resolution", "one_month")
                .encode()
                .toUriString();

        EnergyPrice[] response = restTemplate.getForObject(urlTemplate, EnergyPrice[].class);

        if (response == null) {
            return List.of();
        }

        return Arrays.asList(response);
    }
}
