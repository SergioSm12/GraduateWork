package com.sergio.spring.rest.usuariosvehiculos.app.data;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Capacity;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;

import java.util.Optional;

public class DataCapacities {

    public static Optional<Capacity> createCapacity001() {
        return Optional.of(new Capacity(1L, DataBuilding.createBuilding001().orElseThrow(), new VehicleType(1L, "CARRO"), 100, 100, 0));
    }

    public static Optional<Capacity> createCapacity002() {
        return Optional.of(new Capacity(2L, DataBuilding.createBuilding001().orElseThrow(), new VehicleType(2L, "MOTO"), 80, 80, 0));
    }

    public static Optional<Capacity> createCapacity003() {
        return Optional.of(new Capacity(3L, DataBuilding.createBuilding002().orElseThrow(), new VehicleType(1L, "CARRO"), 90, 90, 0));
    }

    public static Optional<Capacity> createCapacity004() {
        return Optional.of(new Capacity(4L, DataBuilding.createBuilding002().orElseThrow(), new VehicleType(2L, "MOTO"), 50, 50, 0));
    }
}
