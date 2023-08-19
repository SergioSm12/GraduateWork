package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users;

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
}
