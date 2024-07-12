package com.sergio.spring.rest.usuariosvehiculos.app.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "nightly_receipt")
public class NightlyReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @NotNull(message = "No hay un vehiculo seleccionado")
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @NotNull(message = "Debe seleccionar una tarifa")
    @ManyToOne
    @JoinColumn(name = "rate_id")
    private Rate rate;

    private LocalDateTime initialTime;
    private LocalDateTime departureTime;
    private boolean paymentStatus;
    private double amount;

    public NightlyReceipt() {
    }

    public NightlyReceipt(Long id, User user, Vehicle vehicle, Rate rate, LocalDateTime initialTime, LocalDateTime departureTime, boolean paymentStatus, double amount) {
        Id = id;
        this.user = user;
        this.vehicle = vehicle;
        this.rate = rate;
        this.initialTime = initialTime;
        this.departureTime = departureTime;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NightlyReceipt that = (NightlyReceipt) o;
        return paymentStatus == that.paymentStatus && Double.compare(amount, that.amount) == 0 && Objects.equals(Id, that.Id) && Objects.equals(user, that.user) && Objects.equals(vehicle, that.vehicle) && Objects.equals(rate, that.rate) && Objects.equals(initialTime, that.initialTime) && Objects.equals(departureTime, that.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, user, vehicle, rate, initialTime, departureTime, paymentStatus, amount);
    }
}
