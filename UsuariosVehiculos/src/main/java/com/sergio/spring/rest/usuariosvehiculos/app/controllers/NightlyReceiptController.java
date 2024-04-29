package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.sergio.spring.rest.usuariosvehiculos.app.Errors.ErrorResponse;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.NightlyReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.ReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.NightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.NightlyReceiptRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.ReceiptRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.service.INightlyReceiptService;
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
@RequestMapping("/nightly-receipt")
public class NightlyReceiptController {
    @Autowired
    private INightlyReceiptService nightlyReceiptService;
    @Autowired
    private IUserService userService;

    @GetMapping
    public List<NightlyReceiptDto> listNightlyReceipts() {
        return nightlyReceiptService.nightlyReceiptList();
    }

    @GetMapping("/count-unpaid")
    public ResponseEntity<?> getCountUnpaidNightlyReceipts() {
        Long totalUnpaidNightlyReceipts = nightlyReceiptService.getTotalUnpaidNightlyReceipts();
        return ResponseEntity.ok(totalUnpaidNightlyReceipts);
    }

    @GetMapping("/count-paid")
    public ResponseEntity<?> getCountPaidNightlyReceipts() {
        Long totalPaidNightlyReceipts = nightlyReceiptService.getTotalPaidNightlyReceipts();
        return ResponseEntity.ok(totalPaidNightlyReceipts);
    }

    @GetMapping("/count-total")
    public ResponseEntity<?> getTotalNightlyReceipts() {
        Long totalNightlyReceipts = nightlyReceiptService.getTotalNightlyReceipts();
        return ResponseEntity.ok(totalNightlyReceipts);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NightlyReceiptDto>> getNightlyReceiptsByUserId(@PathVariable Long userId) {
        List<NightlyReceiptDto> receipts = nightlyReceiptService.getNightlyReceiptsByUserId(userId);
        return ResponseEntity.ok(receipts);
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<?> createNightlyReceipt(@PathVariable Long userId, @Valid @RequestBody NightlyReceipt nightlyReceipt, BindingResult result) {
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

        //Asignar la entidad user al receipt.
        Optional<User> optionalUser = userService.findByIdUser(userDto.getId());
        User user = optionalUser.orElseThrow();
        nightlyReceipt.setUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(nightlyReceiptService.saveReceipt(nightlyReceipt));
    }

    @PutMapping("/{nightlyReceiptId}/update")
    public ResponseEntity<?> updateReceipt(@Valid @RequestBody NightlyReceiptRequest receipt, BindingResult result,
                                           @PathVariable Long nightlyReceiptId) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<NightlyReceiptDto> ro = nightlyReceiptService.updateNightlyReceipt(receipt, nightlyReceiptId);
        if (ro.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(ro.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/change-payment/{receiptId}")
    public ResponseEntity<?> changePaymentStatus(@PathVariable Long receiptId) {

        Optional<NightlyReceiptDto> NightlyReceiptOptional = nightlyReceiptService.findByIdNightlyReceipt(receiptId);
        if (NightlyReceiptOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        nightlyReceiptService.changePaymentStatus(receiptId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{nightlyReceiptId}")
    public ResponseEntity<?> deleteNightlyReceipt(@PathVariable Long nightlyReceiptId) {
        Optional<NightlyReceiptDto> ro = nightlyReceiptService.findByIdNightlyReceipt(nightlyReceiptId);
        if (ro.isPresent()) {
            nightlyReceiptService.remove(nightlyReceiptId);
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
