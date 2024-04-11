package com.sergio.spring.rest.usuariosvehiculos.app.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "nightly_receipt")
public class NightlyReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank(message = "El campo placa no puede estar vacio")
    @Size(min = 5, max = 6, message = "La placa debe tener entre 5 y 6 caracteres")
    @Pattern(regexp = "^[A-Za-z]{1,3}\\d{1,3}[A-Za-z]?\\d?$", message = "El formato de la placa no es válido")
    private String plate;

    @NotNull(message = "Debe seleccionar una tarifa")
    @ManyToOne
    @JoinColumn(name = "rate_id")
    private  Rate rate;

    private LocalDateTime initialTime;
    private LocalDateTime departureTime;
    private boolean paymentStatus;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
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
}
