package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Building;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IBuildingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/building")
public class BuildingController {
    @Autowired
    private IBuildingService buildingService;

    @GetMapping
    public ResponseEntity<List<Building>> getBuilding() {
        List<Building> buildingList = buildingService.buildingList();
        return new ResponseEntity<>(buildingList, HttpStatus.OK);
    }

    @GetMapping("/{buildingId}")
    public ResponseEntity<?> findBuildingBYId(@PathVariable Long buildingId) {
        Optional<Building> buildingOptional = buildingService.findBuildingById(buildingId);
        if (buildingOptional.isPresent()) {
            return ResponseEntity.ok(buildingOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createBuilding(@Valid @RequestBody Building building, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(buildingService.createBuilding(building));
    }

    @PutMapping("/{buildingId}")
    public ResponseEntity<?> updateBuilding(@Valid @RequestBody Building building, BindingResult result, @PathVariable Long buildingId) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<Building> buildingOptional = buildingService.update(building, buildingId);
        if (buildingOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(buildingOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{buildingId}")
    public ResponseEntity<?> deleteBuilding(@PathVariable Long buildingId) {
        Optional<Building> buildingOptional = buildingService.findBuildingById(buildingId);
        if (buildingOptional.isPresent()) {
            buildingService.deleteBuilding(buildingId);
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
