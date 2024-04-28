package com.sergio.spring.rest.usuariosvehiculos.app.models.request;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class NightlyReceiptRequest {

    @NotNull(message = "El campo hora de ingreso no puede ser nulo")
    private LocalDateTime initialTime;
    @NotNull(message = "El campo hora de salida no puede ser nulo")
    private LocalDateTime departureTime;
    @NotNull
    private boolean paymentStatus;
    private Rate rate;
    private double amount;

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
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
