package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.Errors.NoParkingSpaceException;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataBuilding;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataCapacities;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Capacity;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.ICapacityRepository;
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
class CapacityServiceTest {

    @MockBean
    ICapacityRepository capacityRepository;

    @Autowired
    ICapacityService capacityService;

    @Test
    void testFindAll() {
        //given
        List<Capacity> data = Arrays.asList(
                DataCapacities.createCapacity001().orElseThrow(),
                DataCapacities.createCapacity002().orElseThrow(),
                DataCapacities.createCapacity003().orElseThrow(),
                DataCapacities.createCapacity004().orElseThrow());
        when(capacityRepository.findAll()).thenReturn(data);

        //when
        List<Capacity> capacities = capacityService.findAll();

        //then
        assertFalse(capacities.isEmpty());
        assertEquals(4, capacities.size());
        assertTrue(capacities.contains(DataCapacities.createCapacity001().orElseThrow()));
        verify(capacityRepository).findAll();

    }

    @Test
    void testFindById() {
        //given
        when(capacityRepository.findById(1L)).thenReturn(DataCapacities.createCapacity001());
        when(capacityRepository.findById(4L)).thenReturn(DataCapacities.createCapacity004());

        //when
        Capacity capacity1 = capacityService.findById(1L).orElseThrow();
        Capacity capacity2 = capacityService.findById(4L).orElseThrow();

        assertEquals(1L, capacity1.getId());
        assertEquals("Santo domingo", capacity1.getBuilding().getName());
        assertEquals("CARRO", capacity1.getVehicleType().getName());
        assertEquals(100, capacity1.getParkingSpaces());
        assertEquals(100, capacity1.getMaxParking());
        assertEquals(0, capacity1.getOccupiedSpaces());

        assertEquals(4L, capacity2.getId());
        assertEquals("Giordano bruno", capacity2.getBuilding().getName());
        assertEquals("MOTO", capacity2.getVehicleType().getName());
        assertEquals(50, capacity2.getParkingSpaces());
        assertEquals(50, capacity2.getMaxParking());
        assertEquals(0, capacity2.getOccupiedSpaces());

        verify(capacityRepository, times(2)).findById(anyLong());

    }

    @Test
    void testSave() {
        //given
        Capacity newCapacity = new Capacity(null, DataBuilding.createBuilding001().orElseThrow(), new VehicleType(3L, "Bicileta"), 60, 60, 0);
        when(capacityRepository.save(any())).then(invocationOnMock -> {
            Capacity c = invocationOnMock.getArgument(0);
            c.setId(5L);
            return c;
        });
        //when
        Capacity capacity = capacityService.save(newCapacity);

        //then
        assertEquals(5L, capacity.getId());
        assertEquals("Santo domingo", capacity.getBuilding().getName());
        assertEquals(3L, capacity.getVehicleType().getId());
        assertEquals(60, capacity.getParkingSpaces());
        assertEquals(60, capacity.getMaxParking());
        assertEquals(0, capacity.getOccupiedSpaces());
    }

    @Test
    void testUpdate() {
        //given
        Capacity existsCapacity = DataCapacities.createCapacity001().orElseThrow();
        Capacity updateCapacity = new Capacity(existsCapacity.getId(), DataBuilding.createBuilding002().orElseThrow(), new VehicleType(3L, "Bicicleta"), 70, 70, 0);

        when(capacityRepository.findById(existsCapacity.getId())).thenReturn(Optional.of(existsCapacity));
        when(capacityRepository.save(any())).thenReturn(updateCapacity);

        //when
        Optional<Capacity> result = capacityService.update(updateCapacity, existsCapacity.getId());

        //then
        assertTrue(result.isPresent());
        assertEquals("Giordano bruno", result.get().getBuilding().getName());
        assertEquals("Bicicleta", result.get().getVehicleType().getName());
        assertEquals(70, result.get().getParkingSpaces());
        assertEquals(70, result.get().getMaxParking());
        assertEquals(0, result.get().getOccupiedSpaces());
    }

    @Test
    void deleteCapacity() {
        Long capacityId = 1L;
        doNothing().when(capacityRepository).deleteById(capacityId);

        capacityService.deleteCapacity(capacityId);

        verify(capacityRepository).deleteById(capacityId);

    }

    @Test
    void vehicleEntry() {

        //given
        Capacity capacity = DataCapacities.createCapacity001().orElseThrow();
        Long capacityId = capacity.getId();
        capacity.setParkingSpaces(1);
        when(capacityRepository.findById(capacityId)).thenReturn(Optional.of(capacity));
        //when
        capacityService.vehicleEntry(capacityId);

        assertEquals(0, capacity.getParkingSpaces());
        assertEquals(100, capacity.getMaxParking());
        assertEquals(100 - capacity.getParkingSpaces(), capacity.getOccupiedSpaces());

        verify(capacityRepository).findById(capacityId);
        verify(capacityRepository).save(capacity);

    }

    @Test
    void testVehicleEntryNoSpacesAvailable() {
        Capacity capacity = DataCapacities.createCapacity001().orElseThrow();
        Long capacityId = capacity.getId();
        capacity.setParkingSpaces(0);

        when(capacityRepository.findById(capacityId)).thenReturn(Optional.of(capacity));

        assertThrows(NoParkingSpaceException.class, () -> capacityService.vehicleEntry(capacityId));
        verify(capacityRepository).findById(capacityId);
        verify(capacityRepository, never()).save(any());
    }

    @Test
    void testVehicleExit() {
        Capacity capacity = DataCapacities.createCapacity001().orElseThrow();
        Long capacityId = capacity.getId();
        capacity.setParkingSpaces(99);

        when(capacityRepository.findById(capacityId)).thenReturn(Optional.of(capacity));

        capacityService.vehicleExit(capacityId);

        assertEquals(100, capacity.getParkingSpaces());
        assertEquals(100, capacity.getMaxParking());
        assertEquals(100 - capacity.getParkingSpaces(), capacity.getOccupiedSpaces());

        verify(capacityRepository).findById(capacityId);
        verify(capacityRepository).save(capacity);
    }

    @Test
    void testVehicleExitAllSpacesFree() {
        Capacity capacity = DataCapacities.createCapacity001().orElseThrow();
        Long capacityId = capacity.getId();
        capacity.setParkingSpaces(100);

        when(capacityRepository.findById(capacityId)).thenReturn(Optional.of(capacity));

        assertThrows(RuntimeException.class, () -> capacityService.vehicleExit(capacityId));

        verify(capacityRepository).findById(capacityId);
        verify(capacityRepository, never()).save(any());
    }
}