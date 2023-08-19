package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Faculty;

import java.util.List;
import java.util.Optional;

public interface IFacultyService {
    List<Faculty> findAllFaculties();
    Optional<Faculty> findById(Long id);
    Faculty saveFaculty(Faculty faculty);
    Optional<Faculty> update(Faculty faculty, Long id);
    void remove(Long id);
}
