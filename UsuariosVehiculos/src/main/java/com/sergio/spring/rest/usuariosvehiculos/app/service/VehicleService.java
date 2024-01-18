package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperVehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IUserRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IVehicleRepository;

@Service
public class VehicleService implements IVehicleService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IVehicleRepository vehicleRepository;

    //Listar vehiculos por usuario
    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> findVehiclesByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        List<Vehicle> vehicles = userOptional.get().getVehicles();
        return vehicles.stream().map(v -> DtoMapperVehicleDto.builder().setVehicle(v).build()).collect(Collectors.toList());
    }

    //Listar vehiculos activos por usuario
    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> findActiveVehiclesByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        List<Vehicle> activeVehicles =
                userOptional.map(user -> user.getVehicles().stream().filter(Vehicle::isActive).collect(Collectors.toList()))
                        .orElse(Collections.emptyList());
        return activeVehicles.stream().map(v -> DtoMapperVehicleDto.builder().setVehicle(v).build()).collect(Collectors.toList());
    }

    //Listar vehiculos desactivados por usuario
    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> findInactiveVehiclesByUserId(Long userId) {
        List<Vehicle> inactiveVehicles = vehicleRepository.findByUserIdAndActiveFalse(userId);
        return inactiveVehicles.stream().map(v -> DtoMapperVehicleDto.builder().setVehicle(v).build()).collect(Collectors.toList());

    }

    //listar un vehiculo por id
    @Override
    @Transactional(readOnly = true)
    public Optional<Vehicle> findVehicleByIdAndUserId(Long vehicleId, Long userId) {
        return vehicleRepository.findByIdAndUserId(vehicleId, userId);
    }

    //Guardar vehiculos por usuario
    @Override
    @Transactional
    public VehicleDto saveVehicle(Vehicle vehicle) {
        String plateUpper = vehicle.getPlate().toUpperCase();
        vehicle.setPlate(plateUpper);
        vehicle.setActive(true);

        //optener user y asociarlo
        UserDto userDto = DtoMapperUser.builder().setUser(vehicle.getUser()).build();
        Optional<User> userOptional = userRepository.findById(userDto.getId());

        userOptional.ifPresent(vehicle::setUser);

        return DtoMapperVehicleDto.builder().setVehicle(vehicleRepository.save(vehicle)).build();
    }

    //update vehicles por usuario
    @Override
    @Transactional
    public Optional<VehicleDto> updateVehicle(Long userId, Long vehicleId, Vehicle vehicle) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();
        Vehicle vehicleaUpdate = vehicleOptional.get();
        if (!vehicleaUpdate.getUser().equals(user)) {
            return Optional.empty();
        }

        //Actualizar placa
        vehicleaUpdate.setPlate(vehicle.getPlate());
        //Actualizar type
        if (vehicle.getVehicleType() != null) {
            VehicleType vehicleType = vehicle.getVehicleType();
            vehicleaUpdate.setVehicleType(vehicleType);
        }
        //Actualizar y activar vehiculo
        vehicleaUpdate.setActive(vehicle.isActive());

        Vehicle updateVehicle = vehicleRepository.save(vehicleaUpdate);

        return Optional.of(DtoMapperVehicleDto.builder().setVehicle(updateVehicle).build());
    }


    //Delete vehicle id
    @Override
    @Transactional
    public void removeVehicleByUser(Long vehicleId) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isPresent()) {
            Vehicle vehicle = vehicleOptional.get();
            vehicle.setActive(false);
            vehicleRepository.save(vehicle);
        }

    }

    //Activar Vehiculo
    @Override
    @Transactional
    public void activateVehicleByUser(Long vehicleId) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isPresent()) {
            Vehicle vehicle = vehicleOptional.get();
            vehicle.setActive(true);
            vehicleRepository.save(vehicle);
        }
    }

    //Validar Placa
    @Override
    public boolean existsVehicleWithPlateForUser(Long userId, String plate) {
        return vehicleRepository.existsByUserIdAndPlate(userId, plate);
    }
}
