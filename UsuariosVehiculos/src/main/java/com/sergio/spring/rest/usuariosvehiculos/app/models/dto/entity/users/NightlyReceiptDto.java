package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users;

import java.time.LocalDateTime;

public class NightlyReceiptDto {

    private Long id;
    private UserDto user;
    private VehicleDto vehicle;
    private RateDto rate;
    private LocalDateTime initialTime;
    private LocalDateTime departureTime;
    private boolean paymentStatus;
    private double amount;

    public NightlyReceiptDto() {
    }

    public NightlyReceiptDto(Long id, UserDto user, VehicleDto vehicle, RateDto rate, LocalDateTime initialTime, LocalDateTime departureTime, boolean paymentStatus, double amount) {
        this.id = id;
        this.user = user;
        this.vehicle = vehicle;
        this.rate = rate;
        this.initialTime = initialTime;
        this.departureTime = departureTime;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public VehicleDto getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDto vehicle) {
        this.vehicle = vehicle;
    }

    public RateDto getRate() {
        return rate;
    }

    public void setRate(RateDto rate) {
        this.rate = rate;
    }

    public LocalDateTime getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(LocalDateTime initialTime) {
        this.initialTime = initialTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
