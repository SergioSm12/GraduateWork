package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Capacity;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.ICapacityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CapacityService implements ICapacityService {

    @Autowired
    private ICapacityRepository capacityRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Capacity> findAll() {
        List<Capacity> capacities = capacityRepository.findAll();
        return capacities;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Capacity> findById(Long id) {
        return capacityRepository.findById(id);
    }

    @Override
    @Transactional
    public Capacity save(Capacity capacity) {
        return capacityRepository.save(capacity);
    }

    @Override
    @Transactional
    public Optional<Capacity> update(Capacity capacity, Long id) {
        return capacityRepository.findById(id)
                .map(capacityDb -> {
                    capacityDb.setBuilding(capacity.getBuilding());
                    capacityDb.setVehicleType(capacity.getVehicleType());
                    capacityDb.setParkingSpaces(capacity.getParkingSpaces());
                    return capacityRepository.save(capacityDb);
                });
    }

    @Override
    @Transactional
    public void deleteCapacity(Long id) {
        capacityRepository.deleteById(id);
    }
}
