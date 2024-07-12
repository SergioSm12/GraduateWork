package com.sergio.spring.rest.usuariosvehiculos.app.data;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.NightlyReceipt;

import java.time.LocalDateTime;
import java.util.Optional;

public class DataNightlyReceipt {

    public static Optional<NightlyReceipt> createNightlyReceipt001() {
        LocalDateTime initialTime = LocalDateTime.of(2023, 12, 19, 22, 00);
        LocalDateTime departureTime = LocalDateTime.of(2023, 12, 20, 4, 00);

        return Optional.of(new NightlyReceipt(1L, DataUser.createUser001().orElseThrow(), DataVehicle.createVehicle001().orElseThrow(), DataRate.createRate001().orElseThrow(), initialTime, departureTime,false,18000));
    }

    public static Optional<NightlyReceipt> createNightlyReceipt002() {
        LocalDateTime initialTime = LocalDateTime.of(2024, 5, 4, 22, 00);
        LocalDateTime departureTime = LocalDateTime.of(2024, 5, 5, 7, 00);

        return Optional.of(new NightlyReceipt(2L, DataUser.createUser001().orElseThrow(), DataVehicle.createVehicle004().orElseThrow(), DataRate.createRate002().orElseThrow(), initialTime, departureTime,true,54000));
    }
}
