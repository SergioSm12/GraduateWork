package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Faculty;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IFacultyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/faculty")
public class FacultyController {
    @Autowired
    private IFacultyService facultyService;

    @GetMapping
    public List<Faculty> listFaculties() {
        return facultyService.findAllFaculties();
    }

    @GetMapping("/{facultyId}")
    public ResponseEntity<?> listFaculty(@PathVariable Long facultyId) {
        Optional<Faculty> facultyOptional = facultyService.findById(facultyId);
        if (facultyOptional.isPresent()) {
            return ResponseEntity.ok(facultyOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> saveFaculty(@Valid @RequestBody Faculty faculty, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(facultyService.saveFaculty(faculty));
    }

    @PutMapping("/{facultyId}")
    public ResponseEntity<?> updateFaculty(@Valid @RequestBody Faculty faculty, BindingResult result, @PathVariable Long facultyId) {
        if (result.hasErrors()) {
            return validation(result);
        }

        Optional<Faculty> facultyOptional = facultyService.update(faculty, facultyId);

        if (facultyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(facultyOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{facultyId}")
    public ResponseEntity<?> deleteFaculty(@PathVariable Long facultyId) {
        Optional<Faculty> facultyOptional = facultyService.findById(facultyId);
        if (facultyOptional.isPresent()) {
            facultyService.remove(facultyId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    //Manejo de errors
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo : " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
