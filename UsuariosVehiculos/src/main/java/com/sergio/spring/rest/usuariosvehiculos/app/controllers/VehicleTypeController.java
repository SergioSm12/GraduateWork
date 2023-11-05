package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IVehicleTypeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicleType")
public class VehicleTypeController {

    @Autowired
    private IVehicleTypeService vehicleTypeService;

    @GetMapping()
    public ResponseEntity<?> listVehicleTypes() {
        List<VehicleType> vehicleTypes = vehicleTypeService.findAllVehicleType();
        return ResponseEntity.ok(vehicleTypes);
    }

    @PostMapping()
    public ResponseEntity<?> saveVehicleType(@Valid @RequestBody VehicleType vehicleType, BindingResult result) {

        if (result.hasErrors()) {
            return validation(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleTypeService.saveVehicleType(vehicleType));

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehicleType(@Valid @RequestBody VehicleType vehicleType, BindingResult result,
            @PathVariable Long id) {

        if (result.hasErrors()) {
            validation(result);
        }
        Optional<VehicleType> vt = vehicleTypeService.updateVehicleType(id, vehicleType);
        if (vt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(vt.orElseThrow());
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicleType(@PathVariable Long id) {
        Optional<VehicleType> vt = vehicleTypeService.findVehicleTypeById(id);
        if (vt.isPresent()) {
            vehicleTypeService.deleteVehicleType(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
