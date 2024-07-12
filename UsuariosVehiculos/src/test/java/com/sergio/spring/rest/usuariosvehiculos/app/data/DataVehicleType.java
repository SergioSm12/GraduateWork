package com.sergio.spring.rest.usuariosvehiculos.app.data;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;

import java.util.Optional;

public class DataVehicleType {

    public static Optional<VehicleType> createVehicleType001() {
        return Optional.of(new VehicleType(1L, "MOTO"));
    }

    public static Optional<VehicleType> createVehicleType002() {
        return Optional.of(new VehicleType(2L, "CARRO"));
    }
}
