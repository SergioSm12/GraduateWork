package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.util.List;
import java.util.Optional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;

public interface IVehicleTypeService {
    List<VehicleType> findAllVehicleType();

    Optional<VehicleType> findVehicleTypeById(Long id);

    VehicleType saveVehicleType(VehicleType vehicleType);

    Optional<VehicleType> updateVehicleType(Long vehicleTypeId, VehicleType vehicleType);

    void deleteVehicleType(Long id);
}
