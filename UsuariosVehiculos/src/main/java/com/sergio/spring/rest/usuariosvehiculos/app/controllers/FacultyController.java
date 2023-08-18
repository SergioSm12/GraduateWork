package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Faculty;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IFacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> listFaculty(@PathVariable Long facultyId){
        Optional<Faculty> facultyOptional = facultyService.findById(facultyId);
        if(facultyOptional.isPresent()){
          return ResponseEntity.ok(facultyOptional.orElseThrow());
        }
        return  ResponseEntity.notFound().build();
    }
}
