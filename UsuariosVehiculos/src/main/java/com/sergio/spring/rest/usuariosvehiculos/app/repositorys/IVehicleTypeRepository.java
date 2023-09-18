package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;
import org.springframework.data.repository.CrudRepository;

public interface IVehicleTypeRepository extends CrudRepository<VehicleType, Long> {
}
