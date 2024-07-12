package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.data.DataUser;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IUserRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IVehicleRepository;
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
class VehicleServiceTest {

    @MockBean
    IVehicleRepository vehicleRepository;

    @MockBean
    IUserRepository userRepository;

    @Autowired
    IVehicleService vehicleService;

    @Test
    void testFindVehicles() {
        List<Vehicle> data = Arrays.asList(DataVehicle.createVehicle001().orElseThrow(), DataVehicle.createVehicle002().orElseThrow(), DataVehicle.createVehicle003().orElseThrow(), DataVehicle.createVehicle004().orElseThrow());
        when(vehicleRepository.findAll()).thenReturn(data);

        //when
        List<VehicleDto> vehicles = vehicleService.findVehicles();

        //then
        assertFalse(vehicles.isEmpty());
        assertEquals(4, vehicles.size());
        assertTrue(vehicles.stream().anyMatch(vehicleDto -> vehicleDto.getId().equals(DataVehicle.createVehicle001().orElseThrow().getId())));
        verify(vehicleRepository).findAll();
    }

    @Test
    void testGetTotalRegisteredReceipt() {
        List<Vehicle> vehicles = List.of(
                DataVehicle.createVehicle001().orElseThrow(),
                DataVehicle.createVehicle002().orElseThrow(),
                DataVehicle.createVehicle003().orElseThrow(),
                DataVehicle.createVehicle004().orElseThrow());
        Long totalVehicles = (long) vehicles.size();
        when(vehicleRepository.count()).thenReturn(totalVehicles);

        long result = vehicleService.getTotalRegisteredReceipt();

        assertEquals(totalVehicles, result);
        verify(vehicleRepository).count();
    }

    @Test
    void testFindVehiclesByUserId() {
        User exitUser = DataUser.createUser001().orElseThrow();
        List<Vehicle> vehicles = Arrays.asList(DataVehicle.createVehicle001().orElseThrow(), DataVehicle.createVehicle004().orElseThrow());

        exitUser.setVehicles(vehicles);

        when(userRepository.findById(exitUser.getId())).thenReturn(Optional.of(exitUser));

        List<VehicleDto> result = vehicleService.findVehiclesByUserId(exitUser.getId());

        verify(userRepository).findById(exitUser.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("SZP85E", result.get(0).getPlate());
        assertEquals("BGT420", result.get(1).getPlate());
    }

    @Test
    void testFindActiveVehiclesByUserId() {
        User exitUser = DataUser.createUser001().orElseThrow();
        List<Vehicle> vehicles = Arrays.asList(DataVehicle.createVehicle001().orElseThrow(), DataVehicle.createVehicle004().orElseThrow());

        exitUser.setVehicles(vehicles);

        when(userRepository.findById(exitUser.getId())).thenReturn(Optional.of(exitUser));
        List<VehicleDto> result = vehicleService.findActiveVehiclesByUserId(exitUser.getId());

        verify(userRepository).findById(exitUser.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SZP85E", result.get(0).getPlate());
    }

    @Test
    void findInactiveVehiclesByUserId() {
        User exitUser = DataUser.createUser001().orElseThrow();
        List<Vehicle> vehicles = List.of(DataVehicle.createVehicle004().orElseThrow());

        exitUser.setVehicles(vehicles);

        when(vehicleRepository.findByUserIdAndActiveFalse(exitUser.getId())).thenReturn(vehicles);
        List<VehicleDto> result = vehicleService.findInactiveVehiclesByUserId(exitUser.getId());

        verify(vehicleRepository).findByUserIdAndActiveFalse(exitUser.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BGT420", result.get(0).getPlate());
    }

    @Test
    void testFindVehicleByIdAndUserId() {
        Vehicle vehicle = DataVehicle.createVehicle001().orElseThrow();
        when(vehicleRepository.findByIdAndUserId(vehicle.getId(), vehicle.getUser().getId())).thenReturn(Optional.of(vehicle));

        Vehicle vehicle1 = vehicleService.findVehicleByIdAndUserId(vehicle.getId(), vehicle.getUser().getId()).orElseThrow();

        assertNotNull(vehicle1);
        assertEquals("SZP85E", vehicle1.getPlate());
        assertEquals("Sergio", vehicle1.getUser().getName());
        verify(vehicleRepository).findByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    void testSaveVehicle() {
        Vehicle newVehicle = new Vehicle(null, "XZB44L", DataUser.createUser002().orElseThrow(), DataVehicleType.createVehicleType001().orElseThrow(), true);
        when(vehicleRepository.save(any())).then(invocationOnMock -> {
            Vehicle v = invocationOnMock.getArgument(0);
            v.setId(5L);
            return v;
        });

        VehicleDto vehicle = vehicleService.saveVehicle(newVehicle);

        assertEquals(5L, vehicle.getId());
        assertEquals("XZB44L", vehicle.getPlate());
        assertEquals("MOTO", vehicle.getVehicleType().getName());
        verify(vehicleRepository).save(any());
    }

    @Test
    void updateVehicle() {
        // given
        User existUser = DataUser.createUser001().orElseThrow();
        Vehicle existVehicle = new Vehicle(1L, "SZP85E", existUser, DataVehicleType.createVehicleType001().orElseThrow(), true);
        Vehicle updateVehicle = new Vehicle(existVehicle.getId(), "PRU123", existUser, DataVehicleType.createVehicleType001().orElseThrow(), true);
        existUser.setVehicles(List.of(existVehicle));

        when(userRepository.findById(existUser.getId())).thenReturn(Optional.of(existUser));
        when(vehicleRepository.findById(existVehicle.getId())).thenReturn(Optional.of(existVehicle));
        when(vehicleRepository.save(any())).thenReturn(updateVehicle);

        // when
        Optional<VehicleDto> result = vehicleService.updateVehicle(existUser.getId(), existVehicle.getId(), updateVehicle);

        // then
        verify(userRepository).findById(existUser.getId());
        verify(vehicleRepository).findById(existVehicle.getId());
        verify(vehicleRepository).save(any(Vehicle.class));

        assertTrue(result.isPresent());
        assertEquals("PRU123", result.get().getPlate());
    }

    @Test
    void removeVehicleByUser() {
        Vehicle existVehicle = DataVehicle.createVehicle001().orElseThrow();

        when(vehicleRepository.findById(existVehicle.getId())).thenReturn(Optional.of(existVehicle));

        vehicleService.removeVehicleByUser(existVehicle.getId());

        verify(vehicleRepository).findById(existVehicle.getId());
        assertFalse(existVehicle.isActive());
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void activateVehicleByUser() {
        Vehicle existVehicle = DataVehicle.createVehicle003().orElseThrow();

        when(vehicleRepository.findById(existVehicle.getId())).thenReturn(Optional.of(existVehicle));

        vehicleService.activateVehicleByUser(existVehicle.getId());

        verify(vehicleRepository).findById(existVehicle.getId());
        assertTrue(existVehicle.isActive());
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void existsVehicleWithPlateForUser() {
        User user = DataUser.createUser001().orElseThrow();
        Optional<Vehicle> vehicle = DataVehicle.createVehicle001();
        user.setVehicles(List.of(vehicle.orElseThrow()));

        when(vehicleRepository.existsByUserIdAndPlate(user.getId(), vehicle.get().getPlate())).thenReturn(true);

        boolean result = vehicleService.existsVehicleWithPlateForUser(user.getId(), "SZP85E");

        assertTrue(result);
        verify(vehicleRepository).existsByUserIdAndPlate(anyLong(), anyString());

    }
}