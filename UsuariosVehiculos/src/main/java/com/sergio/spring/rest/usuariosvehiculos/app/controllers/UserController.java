package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IUserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping()
    public List<UserDto> index() {
        return userService.findAll();
    }

    @GetMapping("/count-total")
    public ResponseEntity<?> getTotalUsersCount() {
        Long totalUsers = userService.getTotalCountUsers();
        return ResponseEntity.ok(totalUsers);
    }

    @GetMapping("/page/{page}")
    public Page<UserDto> list(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return userService.findAll(pageable);
    }

    @GetMapping("/active-users")
    public ResponseEntity<List<UserDto>> listActiveUsers() {
        List<UserDto> activeUsers = userService.findActiveUsers();
        return ResponseEntity.ok(activeUsers);
    }

    @GetMapping("/inactive-users")
    public ResponseEntity<List<UserDto>> listDeactivateUsers() {
        List<UserDto> deactivateUsers = userService.findInactiveUsers();
        return ResponseEntity.ok(deactivateUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<UserDto> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        Optional<UserDto> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result, null);
        }

        try {
            UserDto userDto = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(userDto);
        } catch (DataIntegrityViolationException ex) {
            return validation(result, ex);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserRequest user, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validation(result, null);
        }
        Optional<UserDto> o = userService.update(user, id);
        if (o.isPresent()) {

            return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/changePassword/{id}")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserChangePasswordRequest user, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validation(result, null);
        }
        Optional<User> u = userService.findByIdUser(id);
        if (u.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.changePassword(user, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        Optional<UserDto> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        Optional<UserDto> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        Optional<UserDto> o = userService.findById(id);
        if (o.isPresent()) {
            userService.remove(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Metodo para validar errores
    private ResponseEntity<?> validation(BindingResult result, Exception ex) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });

        if (ex instanceof DataIntegrityViolationException) {
            String constraintMessage = ex.getMessage();
            if (constraintMessage.contains("Duplicate entry")) {
                errors.put("email", "Ya hay una cuenta asociada con este correo");
            }
        }

        return ResponseEntity.badRequest().body(errors);
    }

}
