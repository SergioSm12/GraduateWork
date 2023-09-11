package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.sergio.spring.rest.usuariosvehiculos.app.Errors.ErrorResponse;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.ReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;

import com.sergio.spring.rest.usuariosvehiculos.app.models.request.ReceiptRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IReceiptService;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IUserService;
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
@RequestMapping("/receipt")
public class ReceiptController {
    @Autowired
    private IReceiptService receiptService;
    @Autowired
    private IUserService userService;

    //Admin
    @GetMapping
    public List<ReceiptDto> listReceipts() {
        return receiptService.receiptList();
    }

    @GetMapping("/unpaid")
    public List<ReceiptDto> listUnpaidReceipt() {
        return receiptService.getUnpaidReceipts();
    }

    @GetMapping("/paid")
    public List<ReceiptDto> lisPaidReceipt() {
        return receiptService.getPaidReceipts();
    }

    //Obtener  recipts por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReceiptDto>> getReceiptsByUserId(@PathVariable Long userId) {
        List<ReceiptDto> receipts = receiptService.getReceiptsByUserId(userId);
        return ResponseEntity.ok(receipts);
    }

    //Obtener receipts sin pagar por usuario
    @GetMapping("/user/{userId}/unpaid")
    public ResponseEntity<?> getUnpaidReceiptsForUser(@PathVariable Long userId) {
        Optional<UserDto> userDtoOptional = userService.findById(userId);
        if (userDtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UserDto userDto = userDtoOptional.get();
        if (!userDto.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("No tienes permiso para acceder a los permisos de este usuario"));
        }

        Optional<User> userOptional = userService.findByIdUser(userDto.getId());
        User user = userOptional.get();

        List<ReceiptDto> unpaidReceipts = receiptService.getUnpaidReceiptsByUser(user);
        return ResponseEntity.ok(unpaidReceipts);
    }

    //user create
    @PostMapping("/{userId}/create")
    public ResponseEntity<?> createReceiptByUser(@PathVariable Long userId, @Valid @RequestBody Receipt receipt, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<UserDto> userDtoOptional = userService.findById(userId);
        if (userDtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDto userDto = userDtoOptional.get();
        if (!userDto.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("No tienes permiso para crear este usuario"));
        }

        //Asignar la entidad user al Receipt
        Optional<User> optionalUser = userService.findByIdUser(userDto.getId());
        User user = optionalUser.orElseThrow();
        receipt.setUser(user);

        try {
            ReceiptDto creaReceipt = receiptService.saveReceipt(receipt);
            return ResponseEntity.status(HttpStatus.CREATED).body(creaReceipt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error al crear el recibo "));
        }
    }

    //Admin
    @PutMapping("/{receiptId}/update")
    public ResponseEntity<?> updateReceipt(@Valid @RequestBody ReceiptRequest receipt, BindingResult result, @PathVariable Long receiptId) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<ReceiptDto> ro = receiptService.updateReceipt(receipt, receiptId);
        if (ro.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(ro.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    //Admin
    @DeleteMapping("/{receiptId}")
    public ResponseEntity<?> deleteReceipt(@PathVariable Long receiptId) {
        Optional<ReceiptDto> ro = receiptService.findByIdReceipt(receiptId);
        if (ro.isPresent()) {
            receiptService.remove(receiptId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo : " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
