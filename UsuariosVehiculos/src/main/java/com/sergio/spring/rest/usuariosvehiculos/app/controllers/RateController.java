package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.RateDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IRateService;
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
@RequestMapping("/rate")
public class RateController {
    @Autowired
    private IRateService rateService;

    @GetMapping
    public List<RateDto> rateList() {
        return rateService.rateList();
    }

    @GetMapping("/{rateId}")
    public ResponseEntity<?> findRateById(@PathVariable Long rateId) {
        Optional<RateDto> rateOptional = rateService.findRateById(rateId);
        if (rateOptional.isPresent()) {
            return ResponseEntity.ok(rateOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createRate(@Valid @RequestBody Rate rate, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(rateService.createRate(rate));
    }

    @PutMapping("/{rateId}")
    public ResponseEntity<?> updateRate(@Valid @RequestBody Rate rate, BindingResult result, @PathVariable Long rateId) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<RateDto> or = rateService.update(rate, rateId);

        if (or.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(or.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{rateId}")
    public ResponseEntity<?> deleteRate(@PathVariable Long rateId) {
        Optional<RateDto> ro = rateService.findRateById(rateId);
        if (ro.isPresent()) {
            rateService.deleteRate(rateId);
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
