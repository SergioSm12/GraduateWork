package com.sergio.spring.rest.usuariosvehiculos.app.data;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;

import java.util.Optional;

public class DataRate {

    public static Optional<Rate> createRate001() {
        return Optional.of(new Rate(1L, DataVehicleType.createVehicleType001().orElseThrow(), "DIA MOTO", 3000));
    }
    public static Optional<Rate> createRate002() {
        return Optional.of(new Rate(2L, DataVehicleType.createVehicleType002().orElseThrow(), "DIA CARRO", 5000));
    }
}
