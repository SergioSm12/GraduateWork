package com.sergio.spring.rest.usuariosvehiculos.app.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El campo placa no puede estar vacio")
    @Size(min = 5, max = 6, message = "La placa debe tener entre 5 y 6 caracteres")
    @Pattern(regexp = "^[A-Za-z]{1,3}\\d{1,3}[A-Za-z]?\\d?$", message = "El formato de la placa no es válido")
    private String plate;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull(message = "Debe seleccionar un tipo de vehiculo.")
    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleType vehicleType;

    @Column(name = "active")
    private boolean active;

    public Vehicle() {
    }

    public Vehicle(Long id, String plate, User user, VehicleType vehicleType, boolean active) {
        this.id = id;
        this.plate = plate;
        this.user = user;
        this.vehicleType = vehicleType;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return active == vehicle.active && Objects.equals(id, vehicle.id) && Objects.equals(plate, vehicle.plate) && Objects.equals(user, vehicle.user) && Objects.equals(vehicleType, vehicle.vehicleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plate, user, vehicleType, active);
    }
}
