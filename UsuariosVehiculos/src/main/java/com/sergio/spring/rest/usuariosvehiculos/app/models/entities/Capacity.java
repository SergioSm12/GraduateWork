package com.sergio.spring.rest.usuariosvehiculos.app.models.entities;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Capacity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debe seleccionar un edificio")
    @ManyToOne
    private Building building;
    @NotNull(message = "Debe seleccionar un tipo de vehiculo.")
    @ManyToOne
    private VehicleType vehicleType;

    private int parkingSpaces;

    private int maxParking;

    private int occupiedSpaces;


    public Capacity() {
    }

    public Capacity(Long id, Building building, VehicleType vehicleType, int parkingSpaces, int maxParking, int occupiedSpaces) {
        this.id = id;
        this.building = building;
        this.vehicleType = vehicleType;
        this.parkingSpaces = parkingSpaces;
        this.maxParking = maxParking;
        this.occupiedSpaces = occupiedSpaces;
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

    public int getParkingSpaces() {
        return parkingSpaces;
    }


    public void setParkingSpaces(int parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }

    public int getMaxParking() {
        return maxParking;
    }

    public void setMaxParking(int maxParking) {
        this.maxParking = maxParking;
    }

    public int getOccupiedSpaces() {
        return occupiedSpaces;
    }

    public void setOccupiedSpaces(int occupiedSpaces) {
        this.occupiedSpaces = occupiedSpaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Capacity capacity = (Capacity) o;
        return parkingSpaces == capacity.parkingSpaces && maxParking == capacity.maxParking && occupiedSpaces == capacity.occupiedSpaces && Objects.equals(id, capacity.id) && Objects.equals(building, capacity.building) && Objects.equals(vehicleType, capacity.vehicleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, building, vehicleType, parkingSpaces, maxParking, occupiedSpaces);
    }
}
