package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IVehicleTypeRepository;

@Service
public class VehicleTypeService implements IVehicleTypeService {
    @Autowired
    private IVehicleTypeRepository vehicleTypeRepository;

    // Traer vehicleTypes
    @Override
    @Transactional(readOnly = true)
    public List<VehicleType> findAllVehicleType() {
        return (List<VehicleType>) vehicleTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleType> findVehicleTypeById(Long id) {
        return vehicleTypeRepository.findById(id);
    }

    @Override
    @Transactional
    public VehicleType saveVehicleType(VehicleType vehicleType) {
        String nameVehicleUpper = vehicleType.getName().toUpperCase();
        vehicleType.setName(nameVehicleUpper);
        return vehicleTypeRepository.save(vehicleType);
    }

    @Override
    @Transactional
    public Optional<VehicleType> updateVehicleType(Long vehicleTypeId, VehicleType vehicleType) {
        Optional<VehicleType> vehicleTypeOptional = vehicleTypeRepository.findById(vehicleTypeId);
        VehicleType vehicleTypeO = null;
        if (vehicleTypeOptional.isPresent()) {
            VehicleType vehicleTypeBd = vehicleTypeOptional.orElseThrow();
            vehicleTypeBd.setName(vehicleType.getName().toUpperCase());
            vehicleTypeO = vehicleTypeRepository.save(vehicleTypeBd);
        }
        return Optional.ofNullable(vehicleTypeO);
    }

    @Override
    @Transactional
    public void deleteVehicleType(Long id) {

        vehicleTypeRepository.deleteById(id);
    }

}
