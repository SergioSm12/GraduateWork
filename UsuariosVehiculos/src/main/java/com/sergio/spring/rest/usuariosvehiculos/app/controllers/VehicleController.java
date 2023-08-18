package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.sergio.spring.rest.usuariosvehiculos.app.Errors.ErrorResponse;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;

import com.sergio.spring.rest.usuariosvehiculos.app.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        //Agregar User al vehicle
        Optional<User> optionalUser = userService.findByIdUser(userDto.getId());
        User user = optionalUser.get();
        vehicle.setUser(user);

        //validar placa
        boolean vehicleExists = userService.checkIfVehiclePlateExists(vehicle.getPlate());
        if (vehicleExists) {
            return ResponseEntity.badRequest().body(new ErrorResponse("La placa ya existe "));
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveVehicle(vehicle));
    }

    @PutMapping("/{userId}/update/{vehicleId}")
    public ResponseEntity<?> updateVehicleByUser(@PathVariable Long userId, @PathVariable Long vehicleId, @Valid @RequestBody Vehicle vehicle, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
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

        Optional<Vehicle> vehicleOptional = userService.findVehicleByIdAndUserId(vehicleId,userId);
        if (vehicleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.removeVehicleByUser(vehicleId);
        return ResponseEntity.noContent().build();

    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo : " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
