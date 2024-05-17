package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Capacity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ICapacityService {

    List<Capacity> findAll();

    Optional<Capacity> findById(Long id);

    Capacity save(Capacity capacity);

    Optional<Capacity> update(Capacity capacity, Long id);

    void deleteCapacity(Long id);

    //Controlar aforo
    void vehicleEntry(Long capacityId);

    void vehicleExit(Long capacityId);
}
