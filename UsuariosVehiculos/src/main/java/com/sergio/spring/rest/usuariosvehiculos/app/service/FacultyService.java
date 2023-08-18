package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Faculty;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IFacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService implements IFacultyService {

    @Autowired
    private IFacultyRepository facultyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Faculty> findAllFaculties() {
        List<Faculty> faculties = (List<Faculty>) facultyRepository.findAll();
        return faculties;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Faculty> findById(Long id){
        return facultyRepository.findById(id);
    }


}
