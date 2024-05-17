package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.Errors.CapacityNotFoundException;
import com.sergio.spring.rest.usuariosvehiculos.app.Errors.NoParkingSpaceException;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Capacity;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.ICapacityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponse;

import java.util.List;
import java.util.Optional;

@Service
public class CapacityService implements ICapacityService {

    @Autowired
    private ICapacityRepository capacityRepository;

    //administracion
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
        capacity.setMaxParking(capacity.getParkingSpaces());
        int occupied = capacity.getMaxParking() - capacity.getParkingSpaces();
        capacity.setOccupiedSpaces(occupied);
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
                    capacityDb.setMaxParking(capacity.getParkingSpaces());
                    capacityDb.setOccupiedSpaces(0);

                    return capacityRepository.save(capacityDb);
                });
    }

    @Override
    @Transactional
    public void deleteCapacity(Long id) {
        capacityRepository.deleteById(id);
    }

    //Controlar aforo guardia de seguridad
    @Override
    @Transactional
    public void vehicleEntry(Long capacityId) {
        Capacity capacity = capacityRepository.findById(capacityId)
                .orElseThrow(() -> new CapacityNotFoundException("Aforo no encontrado"));
        if (capacity.getParkingSpaces() <= 0) {
            throw new NoParkingSpaceException("No hay espacios disponibles");
        }
        capacity.setParkingSpaces(capacity.getParkingSpaces() - 1);
        int occupied = capacity.getMaxParking() - capacity.getParkingSpaces();
        capacity.setOccupiedSpaces(occupied);
        capacityRepository.save(capacity);
    }

    @Override
    @Transactional
    public void vehicleExit(Long capacityId) {
        Capacity capacity = capacityRepository.findById(capacityId)
                .orElseThrow(() -> new CapacityNotFoundException("Aforo no encontrado"));
        if (capacity.getParkingSpaces() >= capacity.getMaxParking()) {
            throw new RuntimeException("Todas los espacios estan libres");
        }
        capacity.setParkingSpaces(capacity.getParkingSpaces() + 1);
        int occupied = capacity.getMaxParking() - capacity.getParkingSpaces();
        capacity.setOccupiedSpaces(occupied);
        capacityRepository.save(capacity);
    }

}
