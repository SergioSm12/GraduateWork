package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import org.springframework.data.repository.CrudRepository;

public interface IRateRepository extends CrudRepository<Rate, Long> {

}
