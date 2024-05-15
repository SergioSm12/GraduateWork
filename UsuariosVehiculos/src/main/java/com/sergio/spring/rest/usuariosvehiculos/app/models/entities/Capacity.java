package com.sergio.spring.rest.usuariosvehiculos.app.models.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Capacity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Building building;
    @ManyToOne
    private VehicleType vehicleType;

    private int availableCarSpaces;
    private int availableMotorcycleSpaces;

    public Capacity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getAvailableCarSpaces() {
        return availableCarSpaces;
    }

    public void setAvailableCarSpaces(int availableCarSpaces) {
        this.availableCarSpaces = availableCarSpaces;
    }

    public int getAvailableMotorcycleSpaces() {
        return availableMotorcycleSpaces;
    }

    public void setAvailableMotorcycleSpaces(int availableMotorcycleSpaces) {
        this.availableMotorcycleSpaces = availableMotorcycleSpaces;
    }
}
