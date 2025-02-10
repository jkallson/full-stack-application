package com.assignment.backend.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "metering_points")
public class MeteringPointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metering_point_id")
    private Long id;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @OneToMany(mappedBy = "meteringPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsumptionEntity> consumptions;

    public MeteringPointEntity() {}

    public MeteringPointEntity(String address, CustomerEntity customer) {
        this.address = address;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public List<ConsumptionEntity> getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(List<ConsumptionEntity> consumptions) {
        this.consumptions = consumptions;
    }
}
