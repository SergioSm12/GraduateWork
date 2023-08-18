package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Faculty;

import java.util.List;
import java.util.Optional;

public interface IFacultyService {
    List<Faculty> findAllFaculties();
    Optional<Faculty> findById(Long id);
}
