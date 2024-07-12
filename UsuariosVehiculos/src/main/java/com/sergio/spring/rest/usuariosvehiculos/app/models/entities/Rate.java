package com.sergio.spring.rest.usuariosvehiculos.app.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

@Entity
@Table(name = "rates")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Debe seleccionar un tipo de vehiculo.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehcile_type_id")
    private VehicleType vehicleType;
    @NotBlank(message = "El campo tiempo no puede estar vacio")
    private String time;
    @NotNull(message = "El campo valor no puede estar vacio")
    @Positive(message = "El valor debe ser mayor a cero.")
    private double amount;


    public Rate() {
    }

    public Rate(Long id, VehicleType vehicleType, String time, double amount) {
        this.id = id;
        this.vehicleType = vehicleType;
        this.time = time;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Double.compare(amount, rate.amount) == 0 && Objects.equals(id, rate.id) && Objects.equals(vehicleType, rate.vehicleType) && Objects.equals(time, rate.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vehicleType, time, amount);
    }
}
