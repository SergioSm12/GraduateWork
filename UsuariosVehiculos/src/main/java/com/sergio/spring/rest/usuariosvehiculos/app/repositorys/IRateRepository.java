package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import org.springframework.data.repository.CrudRepository;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;

public interface IRateRepository extends CrudRepository<Rate, Long> {

}
