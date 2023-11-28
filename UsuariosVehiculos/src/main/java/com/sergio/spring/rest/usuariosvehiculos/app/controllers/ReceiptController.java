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

import com.sergio.spring.rest.usuariosvehiculos.app.Errors.ErrorResponse;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.ReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.ReceiptRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IReceiptService;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IUserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/receipt")
public class ReceiptController {
    @Autowired
    private IReceiptService receiptService;
    @Autowired
    private IUserService userService;

    // Admin
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

    // Obtener recipts por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReceiptDto>> getReceiptsByUserId(@PathVariable Long userId) {
        List<ReceiptDto> receipts = receiptService.getReceiptsByUserId(userId);
        return ResponseEntity.ok(receipts);
    }

    // Obtener receipts sin pagar por usuario
    @GetMapping("/user/{userId}/unpaid")
    public ResponseEntity<?> getUnpaidReceiptsForUser(@PathVariable Long userId) {
        Optional<UserDto> userDtoOptional = userService.findById(userId);
        if (userDtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UserDto userDto = userDtoOptional.get();
        if (!userDto.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("No tienes permiso para acceder a los permisos de este usuario"));
        }

        Optional<User> userOptional = userService.findByIdUser(userDto.getId());
        User user = userOptional.get();

        List<ReceiptDto> unpaidReceipts = receiptService.getUnpaidReceiptsByUser(user);
        return ResponseEntity.ok(unpaidReceipts);
    }

    // Obtener total de recibos no pagos
    @GetMapping("/count-unpaid")
    public ResponseEntity<?> getCountUnpaidReceipts() {
        Long totalUnpaidReceipts = receiptService.getTotalUnpaidReceipts();
        return ResponseEntity.ok(totalUnpaidReceipts);
    }

    @GetMapping("/count-paid")
    public ResponseEntity<?> getCountPaidReceipts() {
        Long totalPaidReceipts = receiptService.getTotalPaidReceipts();
        return ResponseEntity.ok(totalPaidReceipts);
    }

    @GetMapping("/count-total")
    public ResponseEntity<?> getTotalReceipts() {
        Long totalReceipts = receiptService.getTotalReceipts();
        return ResponseEntity.ok(totalReceipts);
    }

    // user create
    @PostMapping("/{userId}/create")
    public ResponseEntity<?> createReceiptByUser(@PathVariable Long userId, @Valid @RequestBody Receipt receipt,
            BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        Optional<UserDto> userDtoOptional = userService.findById(userId);
        if (userDtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDto userDto = userDtoOptional.get();
        if (!userDto.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("Usuario no encontrado"));
        }

        // Asignar la entidad user al Receipt
        Optional<User> optionalUser = userService.findByIdUser(userDto.getId());
        User user = optionalUser.orElseThrow();
        receipt.setUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(receiptService.saveReceipt(receipt));
    }

    // Admin update
    @PutMapping("/{receiptId}/update")
    public ResponseEntity<?> updateReceipt(@Valid @RequestBody ReceiptRequest receipt, BindingResult result,
            @PathVariable Long receiptId) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<ReceiptDto> ro = receiptService.updateReceipt(receipt, receiptId);
        if (ro.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(ro.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    // admin changePaymentStatus cambia el estado de pago
    @PutMapping("/change-payment/{receiptId}")
    public ResponseEntity<?> changePaymentStatus(@PathVariable Long receiptId) {

        Optional<ReceiptDto> receiptOptional = receiptService.findByIdReceipt(receiptId);
        if (receiptOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        receiptService.changePaymentStatus(receiptId);
        return ResponseEntity.ok().build();
    }

    // Admin
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
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
