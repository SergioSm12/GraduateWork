package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    //Users
    List<UserDto> findAll();

    Page<UserDto> findAll(Pageable pageable);

    Optional<UserDto> findById(Long id);

    Optional<User> findByIdUser(Long id);

    UserDto save(User user);

    Optional<UserDto> update(UserRequest user, Long id);

    void remove(Long id);

    //Vehicles
    List<VehicleDto> findVehiclesByUserId(Long userId);

    List<VehicleDto> findActiveVehiclesByUserId(Long userId);

    List<VehicleDto> findInactiveVehiclesByUserId(Long userId);

    Optional<Vehicle> findVehicleByIdAndUserId(Long vehicleId, Long userId);

    VehicleDto saveVehicle(Vehicle vehicle);

    Optional<VehicleDto> updateVehicle(Long userId, Long vehicleId, Vehicle vehicle);

    void removeVehicleByUser(Long vehicleId);

    void activateVehicleByUser(Long vehicleId);

    boolean existsVehicleWithPlateForUser(Long userId, String plate);

    //vehicle type
    List<VehicleType> findAllVehicleType();


}
