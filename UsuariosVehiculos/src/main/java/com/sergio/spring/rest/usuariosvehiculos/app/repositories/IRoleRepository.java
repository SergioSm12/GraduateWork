package com.sergio.spring.rest.usuariosvehiculos.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Role;

public interface IRoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);


}
