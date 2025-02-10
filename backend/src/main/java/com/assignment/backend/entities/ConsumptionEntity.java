package com.assignment.backend.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "consumption")
public class ConsumptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumption_id")
    private Long id;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "amount_unit", nullable = false)
    private String amountUnit;

    @Column(name = "consumption_time", nullable = false)
    private Instant consumptionTime;

    @ManyToOne
    @JoinColumn(name = "metering_point_id", nullable = false)
    private MeteringPointEntity meteringPoint;

    public ConsumptionEntity() {}

    public ConsumptionEntity(Integer amount, String amountUnit, Instant consumptionTime, MeteringPointEntity meteringPoint) {
        this.amount = amount;
        this.amountUnit = amountUnit;
        this.consumptionTime = consumptionTime;
        this.meteringPoint = meteringPoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit;
    }

    public Instant getConsumptionTime() {
        return consumptionTime;
    }

    public void setConsumptionTime(Instant consumptionTime) {
        this.consumptionTime = consumptionTime;
    }

    public MeteringPointEntity getMeteringPoint() {
        return meteringPoint;
    }

    public void setMeteringPoint(MeteringPointEntity meteringPoint) {
        this.meteringPoint = meteringPoint;
    }
}
