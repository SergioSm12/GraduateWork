package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Building;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IBuildingService {

    List<Building> buildingList();

    Optional<Building> findBuildingById(Long buildingId);

    Building createBuilding(Building building);

    Optional<Building> update(Building building, Long id);

    void deleteBuilding(Long id);
}
