package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Capacity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICapacityRepository extends JpaRepository<Capacity, Long> {

}
