package com.sergio.spring.rest.usuariosvehiculos.app.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "nightly_receipt")
public class NightlyReceipt extends BaseReceipt {
    private LocalDateTime initialTime;
    private LocalDateTime departureTime;
    private double amount;

    public NightlyReceipt() {
    }

    public NightlyReceipt(Long id, User user, Vehicle vehicle, Rate rate, LocalDateTime initialTime, LocalDateTime departureTime, boolean paymentStatus, double amount) {
        super.setId(id);
        super.setUser(user);
        super.setVehicle(vehicle);
        super.setRate(rate);
        this.initialTime = initialTime;
        this.departureTime = departureTime;
        super.setPaymentStatus(paymentStatus);
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
