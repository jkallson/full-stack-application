package com.assignment.backend.dtos;

import java.time.ZonedDateTime;

public record EnergyPriceDto(
        Double centsPerKwh,
        Double centsPerKwhWithVat,
        Double eurPerMwh,
        Double eurPerMwhWithVat,
        ZonedDateTime fromDateTime,
        ZonedDateTime toDateTime
) {
    public boolean includes(ZonedDateTime time) {
        return !time.isBefore(this.fromDateTime) && !time.isAfter(this.toDateTime);
    }
}
