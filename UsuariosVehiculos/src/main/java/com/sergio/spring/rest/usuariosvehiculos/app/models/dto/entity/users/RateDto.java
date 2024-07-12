package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users;

import java.util.Objects;

public class RateDto {
    private Long id;
    private String time;
    private double amount;
    private VehicleTypeDto vehicleType;

    public RateDto() {
    }

    public RateDto(Long id, String time, double amount, VehicleTypeDto vehicleType) {
        this.id = id;
        this.time = time;
        this.amount = amount;
        this.vehicleType = vehicleType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public VehicleTypeDto getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleTypeDto vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateDto rateDto = (RateDto) o;
        return Double.compare(amount, rateDto.amount) == 0 && Objects.equals(id, rateDto.id) && Objects.equals(time, rateDto.time) && Objects.equals(vehicleType, rateDto.vehicleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, amount, vehicleType);
    }
}
