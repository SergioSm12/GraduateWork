package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.data.DataBuilding;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Building;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IBuildingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BuildingServiceTest {


    @MockBean
    IBuildingRepository buildingRepository;

    @Autowired
    IBuildingService buildingService;

    @Test
    void testBuildingList() {
        //GIVEN
        List<Building> data = Arrays.asList(DataBuilding.createBuilding001().orElseThrow(), DataBuilding.createBuilding002().orElseThrow());
        when(buildingRepository.findAll()).thenReturn(data);

        //when
        List<Building> buildings = buildingService.buildingList();


        //then
        assertFalse(buildings.isEmpty());
        assertEquals(2, buildings.size());
        assertTrue(buildings.contains(DataBuilding.createBuilding001().orElseThrow()));
        verify(buildingRepository).findAll();
    }

    @Test
    void testBuildingById() {
        //given
        when(buildingRepository.findById(1L)).thenReturn(DataBuilding.createBuilding001());
        when(buildingRepository.findById(2L)).thenReturn(DataBuilding.createBuilding002());

        //when
        String nameBuilding = buildingService.findBuildingById(1L).orElseThrow().getName();
        Long idBuilding = buildingService.findBuildingById(2L).orElseThrow().getId();

        //then
        assertEquals("Santo domingo", nameBuilding);
        assertEquals(2L, idBuilding);
        verify(buildingRepository, times(2)).findById(anyLong());
    }

    @Test
    void testCreateBuilding() {
        //given
        Building newBuilding = new Building(null, "San Alberto Magno");
        when(buildingRepository.save(any())).then(invocationOnMock -> {
            Building b = invocationOnMock.getArgument(0);
            b.setId(3L);
            return b;
        });

        //when
        Building building = buildingService.createBuilding(newBuilding);

        //then
        assertEquals(3L, building.getId());
        assertEquals("SAN ALBERTO MAGNO", building.getName());
        verify(buildingRepository).save(any());
    }

    @Test
    void testUpdateBuilding() {
        //given
        Building existsBuilding = DataBuilding.createBuilding001().orElseThrow();
        Building updateBuilding = new Building(existsBuilding.getId(), "update building");

        when(buildingRepository.findById(existsBuilding.getId())).thenReturn(Optional.of(existsBuilding));
        when(buildingRepository.save(any())).thenReturn(updateBuilding);

        //when
        Optional<Building> result = buildingService.update(updateBuilding, existsBuilding.getId());

        //then
        assertTrue(result.isPresent());
        assertEquals("update building", result.get().getName());
        verify(buildingRepository).findById(existsBuilding.getId());
        verify(buildingRepository).save(existsBuilding);
    }

    @Test
    void testDeleteBuilding() {
        //given
        Long buildingId = 1L;
        doNothing().when(buildingRepository).deleteById(buildingId);

        //when
        buildingService.deleteBuilding(buildingId);

        //then
        verify(buildingRepository).deleteById(buildingId);
    }
}