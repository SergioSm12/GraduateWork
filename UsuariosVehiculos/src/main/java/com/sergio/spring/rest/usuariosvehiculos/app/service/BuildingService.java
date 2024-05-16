package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Building;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IBuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BuildingService implements IBuildingService {

    @Autowired
    private IBuildingRepository buildingRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Building> buildingList() {
        List<Building> buildings = (List<Building>) buildingRepository.findAll();
        return buildings;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Building> findBuildingById(Long buildingId) {
        return buildingRepository.findById(buildingId);
    }

    @Override
    @Transactional
    public Building createBuilding(Building building) {
        String nameUpper = building.getName().toUpperCase();
        building.setName(nameUpper);
        return buildingRepository.save(building);
    }

    @Override
    @Transactional
    public Optional<Building> update(Building building, Long id) {
        return buildingRepository.findById(id)
                .map(buildingDB -> {
                    buildingDB.setName(building.getName().toUpperCase());
                    return buildingRepository.save(buildingDB);
                });
    }

    @Override
    @Transactional
    public void deleteBuilding(Long id) {
        buildingRepository.deleteById(id);
    }


}
