package com.assignment.backend.dtos;

import java.time.ZonedDateTime;

public record ConsumptionDto (Integer amount, String unit, ZonedDateTime time) {}
