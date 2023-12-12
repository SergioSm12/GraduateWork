package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sergio.spring.rest.usuariosvehiculos.app.Errors.ErrorResponse;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IUserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/vehicle")
public class VehicleController {
    @Autowired
    private IUserService userService;

    

    @GetMapping("/{userId}/list")
    public ResponseEntity<?> listVehiclesByUser(@PathVariable Long userId) {
        Optional<UserDto> userDtoOptional = userService.findById(userId);
        if (userDtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<VehicleDto> vehicles = userService.findVehiclesByUserId(userId);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{userId}/active-vehicles")
    public ResponseEntity<List<VehicleDto>> listActiveVehiclesByUser(@PathVariable Long userId) {
        List<VehicleDto> activeVehicles = userService.findActiveVehiclesByUserId(userId);
        return ResponseEntity.ok(activeVehicles);
    }

    @GetMapping("/{userId}/inactive-vehicles")
    public ResponseEntity<List<VehicleDto>> listInactiveVehiclesByUser(@PathVariable Long userId) {
        List<VehicleDto> inactiveVehicles = userService.findInactiveVehiclesByUserId(userId);
        return ResponseEntity.ok(inactiveVehicles);
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<?> createVehicleByUser(@PathVariable Long userId, @Valid @RequestBody Vehicle vehicle, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        //Validar usuario para permitir crear
        Optional<UserDto> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDto userDto = userOptional.get();
        if (!userDto.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("No tienes permiso para crear un vehiculo con este usuario"));
        }

        //Validar si el usuario ya tiene un vehiculo con la misma placa
        if (userService.existsVehicleWithPlateForUser(userId, vehicle.getPlate())) {
            return validation("plate", "Ya tiene un vehiculo registrado con la misma placa");
        }

        //Agregar User al vehicle
        Optional<User> optionalUser = userService.findByIdUser(userDto.getId());
        User user = optionalUser.get();
        vehicle.setUser(user);


        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveVehicle(vehicle));

    }

    @PutMapping("/{userId}/update/{vehicleId}")
    public ResponseEntity<?> updateVehicleByUser(@PathVariable Long userId, @PathVariable Long vehicleId, @Valid @RequestBody Vehicle vehicle, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        //Validar si el usuario ya tiene un vehiculo con la misma placa
        if (userService.existsVehicleWithPlateForUser(userId, vehicle.getPlate())) {
            return validation("plate", "Ya tiene un vehiculo registrado con la misma placa");
        }
        vehicle.setPlate(vehicle.getPlate().toUpperCase());
        Optional<VehicleDto> updateVehicleOptional = userService.updateVehicle(userId, vehicleId, vehicle);
        if (updateVehicleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updateVehicleOptional.get());
    }

    @Transactional
    @DeleteMapping("/{userId}/delete/{vehicleId}")
    public ResponseEntity<?> deleteVehicleByUser(@PathVariable Long userId, @PathVariable Long vehicleId) {
        Optional<UserDto> userDtoOptional = userService.findById(userId);
        if (userDtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Vehicle> vehicleOptional = userService.findVehicleByIdAndUserId(vehicleId, userId);
        if (vehicleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.removeVehicleByUser(vehicleId);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{userId}/activate-vehicle/{vehicleId}")
    public ResponseEntity<?> activateVehicleByUser(@PathVariable Long userId, @PathVariable Long vehicleId) {
        Optional<UserDto> userDtoOptional = userService.findById(userId);
        if (userDtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Vehicle> vehicleOptional = userService.findVehicleByIdAndUserId(vehicleId, userId);
        if (vehicleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.activateVehicleByUser(vehicleId);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    private ResponseEntity<?> validation(String fieldName, String errorMessage) {
        Map<String, String> error = new HashMap<>();
        error.put(fieldName, errorMessage);
        return ResponseEntity.badRequest().body(error);
    }
    


}
