package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VisitorReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IVisitorReceiptService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/visitor-receipt")
public class VisitorReceiptController {
    @Autowired
    private IVisitorReceiptService visitorReceiptService;

    @GetMapping
    public List<VisitorReceiptDto> listVisitorReceipts() {
        return visitorReceiptService.visitorReceiptList();
    }

    @PostMapping
    public ResponseEntity<?> createVisitorReceipt(@Valid @RequestBody VisitorReceipt visitorReceipt,
            BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(visitorReceiptService.saveVisitorReceipt(visitorReceipt));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVisitorReceipt(@Valid @RequestBody VisitorReceipt visitorReceipt,
            BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<VisitorReceiptDto> vro = visitorReceiptService.updateVisitorReceipt(visitorReceipt, id);
        if (vro.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(vro.orElseThrow());
        }
        return ResponseEntity.notFound().build();

    }

    @PutMapping("/change-payment/{visitorReceiptId}")
    public ResponseEntity<?> changePaymentStatus(@PathVariable Long visitorReceiptId) {
        Optional<VisitorReceiptDto> visitorReceiptOptional = visitorReceiptService
                .findByIdVisitorReceipt(visitorReceiptId);
        if (visitorReceiptOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        visitorReceiptService.changePaymentStatus(visitorReceiptId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{visitorReceiptId}")
    public ResponseEntity<?> deleteReceipt(@PathVariable Long visitorReceiptId) {
        Optional<VisitorReceiptDto> visitorReceiptOptional = visitorReceiptService
                .findByIdVisitorReceipt(visitorReceiptId);
        if (visitorReceiptOptional.isPresent()) {
            visitorReceiptService.removeVisitorReceipt(visitorReceiptId);
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
