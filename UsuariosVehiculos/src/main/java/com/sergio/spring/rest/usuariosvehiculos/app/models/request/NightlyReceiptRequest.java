package com.sergio.spring.rest.usuariosvehiculos.app.models.request;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class NightlyReceiptRequest {

    @NotNull(message = "El campo hora de ingreso no puede ser nulo")
    private LocalDateTime initialTime;
    @NotNull(message = "El campo hora de salida no puede ser nulo")
    private LocalDateTime departureTime;
    @NotNull
    private boolean paymentStatus;
    @NotNull(message = "Debe seleccionar una tarifa")
    private Rate rate;
    private double amount;

    public NightlyReceiptRequest() {
    }

    public NightlyReceiptRequest(LocalDateTime initialTime, LocalDateTime departureTime, boolean paymentStatus, Rate rate, double amount) {
        this.initialTime = initialTime;
        this.departureTime = departureTime;
        this.paymentStatus = paymentStatus;
        this.rate = rate;
        this.amount = amount;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NightlyReceiptRequest that = (NightlyReceiptRequest) o;
        return paymentStatus == that.paymentStatus && Double.compare(amount, that.amount) == 0 && Objects.equals(initialTime, that.initialTime) && Objects.equals(departureTime, that.departureTime) && Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialTime, departureTime, paymentStatus, rate, amount);
    }
}
