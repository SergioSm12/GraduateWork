package com.sergio.spring.rest.usuariosvehiculos.app.data;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Building;

import java.util.Optional;

public class DataBuilding {

    public static Optional<Building> createBuilding001() {
        return Optional.of(new Building(1L, "Santo domingo"));
    }

    public static Optional<Building> createBuilding002() {
        return Optional.of(new Building(2L, "Giordano bruno"));
    }
}
