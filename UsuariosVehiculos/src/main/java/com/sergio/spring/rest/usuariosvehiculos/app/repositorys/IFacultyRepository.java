package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Faculty;
import org.springframework.data.repository.CrudRepository;

public interface IFacultyRepository extends CrudRepository<Faculty,Long> {
}
