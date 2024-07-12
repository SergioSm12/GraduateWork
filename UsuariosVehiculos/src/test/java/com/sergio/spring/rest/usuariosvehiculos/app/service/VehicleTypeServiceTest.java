package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IVehicleTypeRepository;
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
class VehicleTypeServiceTest {

    @MockBean
    IVehicleTypeRepository vehicleTypeRepository;

    @Autowired
    IVehicleTypeService vehicleTypeService;

    @Test
    void testFindAllVehicleType() {

        //given
        List<VehicleType> data = Arrays.asList(DataVehicleType.createVehicleType001().orElseThrow(), DataVehicleType.createVehicleType002().orElseThrow());
        when(vehicleTypeRepository.findAll()).thenReturn(data);

        //when
        List<VehicleType> vehicleTypes = vehicleTypeService.findAllVehicleType();

        //then
        assertFalse(vehicleTypes.isEmpty());
        assertEquals(2, vehicleTypes.size());
        assertTrue(vehicleTypes.contains(DataVehicleType.createVehicleType002().orElseThrow()));
        verify(vehicleTypeRepository).findAll();

    }

    @Test
    void testFindVehicleTypeById() {
        //given
        when(vehicleTypeRepository.findById(1L)).thenReturn(DataVehicleType.createVehicleType001());
        when(vehicleTypeRepository.findById(2L)).thenReturn(DataVehicleType.createVehicleType002());

        //when
        VehicleType vehicleType1 = vehicleTypeService.findVehicleTypeById(1L).orElseThrow();
        VehicleType vehicleType2 = vehicleTypeService.findVehicleTypeById(2L).orElseThrow();

        //then
        assertEquals(1L, vehicleType1.getId());
        assertEquals("MOTO", vehicleType1.getName());

        assertEquals(2L, vehicleType2.getId());
        assertEquals("CARRO", vehicleType2.getName());

        verify(vehicleTypeRepository, times(2)).findById(anyLong());
    }


    @Test
    void testSaveVehicleType() {
        //given
        VehicleType newVehicleType = new VehicleType(null, "Bicicleta");
        when(vehicleTypeRepository.save(any())).then(invocationOnMock -> {
            VehicleType vt = invocationOnMock.getArgument(0);
            vt.setId(3L);
            return vt;
        });

        //when
        VehicleType vehicleType = vehicleTypeService.saveVehicleType(newVehicleType);

        //then
        assertEquals(3L, vehicleType.getId());
        assertEquals("BICICLETA", vehicleType.getName());
    }

    @Test
    void testUpdateVehicleType() {
        //given
        VehicleType vehicleType = DataVehicleType.createVehicleType001().orElseThrow();
        VehicleType updateVehicleType = new VehicleType(vehicleType.getId(), "BICICLETA");

        when(vehicleTypeRepository.findById(vehicleType.getId())).thenReturn(Optional.of(vehicleType));
        when(vehicleTypeRepository.save(any())).thenReturn(updateVehicleType);

        //when
        Optional<VehicleType> result = vehicleTypeService.updateVehicleType(vehicleType.getId(), updateVehicleType);

        //then
        assertTrue(result.isPresent());
        assertEquals("BICICLETA", result.get().getName());
    }

    @Test
    void deleteVehicleType() {
        Long vehicleTypeId = 1L;
        doNothing().when(vehicleTypeRepository).deleteById(vehicleTypeId);

        vehicleTypeService.deleteVehicleType(vehicleTypeId);

        verify(vehicleTypeRepository).deleteById(vehicleTypeId);
    }

}