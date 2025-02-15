package com.assignment.backend.dtos;

import java.time.ZonedDateTime;

public record EnergyPrice (
        Double centsPerKwh,
        Double centsPerKwhWithVat,
        Double eurPerMwh,
        Double eurPerMwhWithVat,
        ZonedDateTime fromDateTime,
        ZonedDateTime toDateTime
) {}
