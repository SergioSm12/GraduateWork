package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.util.List;
import java.util.Optional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;

public interface IVehicleService {
    // Vehicles

    List<VehicleDto> findVehicles();

    long getTotalRegisteredReceipt();
    List<VehicleDto> findVehiclesByUserId(Long userId);

    List<VehicleDto> findActiveVehiclesByUserId(Long userId);

    List<VehicleDto> findInactiveVehiclesByUserId(Long userId);

    Optional<Vehicle> findVehicleByIdAndUserId(Long vehicleId, Long userId);

    VehicleDto saveVehicle(Vehicle vehicle);

    Optional<VehicleDto> updateVehicle(Long userId, Long vehicleId, Vehicle vehicle);

    void removeVehicleByUser(Long vehicleId);

    void activateVehicleByUser(Long vehicleId);

    boolean existsVehicleWithPlateForUser(Long userId, String plate);
}
