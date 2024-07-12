package com.sergio.spring.rest.usuariosvehiculos.app.data;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;

import java.util.Optional;

public class DataVehicle {

    public static Optional<Vehicle> createVehicle001() {
        return Optional.of(new Vehicle(1L, "SZP85E", DataUser.createUser001().orElseThrow(), DataVehicleType.createVehicleType001().orElseThrow(), true));
    }

    public static Optional<Vehicle> createVehicle002() {
        return Optional.of(new Vehicle(2L, "QAS874", DataUser.createUser002().orElseThrow(), DataVehicleType.createVehicleType002().orElseThrow(), true));
    }

    public static Optional<Vehicle> createVehicle003() {
        return Optional.of(new Vehicle(3L, "LHY85T", DataUser.createUser002().orElseThrow(), DataVehicleType.createVehicleType001().orElseThrow(), false));
    }

    public static Optional<Vehicle> createVehicle004() {
        return Optional.of(new Vehicle(4L, "BGT420", DataUser.createUser001().orElseThrow(), DataVehicleType.createVehicleType002().orElseThrow(), false));
    }
}
