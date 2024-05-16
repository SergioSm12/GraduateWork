package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Capacity;
import com.sergio.spring.rest.usuariosvehiculos.app.service.ICapacityService;
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
@RequestMapping("/capacity")
public class CapacityController {
    @Autowired
    private ICapacityService capacityService;

    @GetMapping
    public ResponseEntity<List<Capacity>> findAllCapacity() {
        return new ResponseEntity<>(capacityService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{capacityId}")
    public ResponseEntity<?> findCapacityById(@PathVariable Long capacityId) {
        Optional<Capacity> capacityOptional = capacityService.findById(capacityId);
        if (capacityOptional.isPresent()) {
            return ResponseEntity.ok(capacityOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createCapacity(@Valid @RequestBody Capacity capacity, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(capacityService.save(capacity));
    }

    @PutMapping("/{capacityId}")
    public ResponseEntity<?> updateCapacity(@Valid @RequestBody Capacity capacity, BindingResult result, @PathVariable Long capacityId) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<Capacity> capacityOptional = capacityService.update(capacity, capacityId);
        if (capacityOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(capacityOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{capacityId}")
    public ResponseEntity<?> deleteCapacity(@PathVariable Long capacityId) {
        Optional<Capacity> capacityOptional = capacityService.findById(capacityId);
        if (capacityOptional.isPresent()) {
            capacityService.deleteCapacity(capacityId);
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
