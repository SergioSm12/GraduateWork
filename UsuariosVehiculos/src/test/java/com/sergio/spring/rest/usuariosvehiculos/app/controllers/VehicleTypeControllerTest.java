package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IVehicleTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(VehicleTypeController.class)
class VehicleTypeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IVehicleTypeService vehicleTypeService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser
    void listVehicleTypes() throws Exception {
        List<VehicleType> vehicleTypes = Arrays.asList(DataVehicleType.createVehicleType001().orElseThrow(), DataVehicleType.createVehicleType002().orElseThrow());
        when(vehicleTypeService.findAllVehicleType()).thenReturn(vehicleTypes);

        mvc.perform(get("/vehicleType").contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("MOTO"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("CARRO"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(vehicleTypes)));

        verify(vehicleTypeService).findAllVehicleType();
    }

    @Test
    @WithMockUser
    void saveVehicleType() throws Exception {
        VehicleType vehicleType = new VehicleType(null, "BICICLETA");
        when(vehicleTypeService.saveVehicleType(vehicleType)).then(invocationOnMock -> {
            VehicleType vt = invocationOnMock.getArgument(0);
            vt.setId(3L);
            return vt;
        });

        mvc.perform(post("/vehicleType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleType))
                        .with(csrf()))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("BICICLETA"));
        verify(vehicleTypeService).saveVehicleType(any(VehicleType.class));

    }

    @Test
    @WithMockUser
    void testSaveVehicleTypeValidationErrors() throws Exception {
        VehicleType vehicleType = new VehicleType(null, null);
        when(vehicleTypeService.saveVehicleType(vehicleType)).then(invocationOnMock -> {
            VehicleType vt = invocationOnMock.getArgument(0);
            vt.setId(3L);
            return vt;
        });

        mvc.perform(post("/vehicleType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleType))
                        .with(csrf()))

                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("must not be blank"));
        verify(vehicleTypeService, never()).saveVehicleType(any(VehicleType.class));
    }

    @Test
    @WithMockUser
    void updateVehicleType() throws Exception {
        VehicleType existingVehicleType = DataVehicleType.createVehicleType001().orElseThrow();
        VehicleType updateVehicleType = new VehicleType(null, "BICICLETA");
        VehicleType savedVehicleType = new VehicleType();
        savedVehicleType.setId(existingVehicleType.getId());
        savedVehicleType.setName(updateVehicleType.getName());

        when(vehicleTypeService.updateVehicleType(existingVehicleType.getId(), updateVehicleType)).thenReturn(Optional.of(savedVehicleType));

        mvc.perform(put("/vehicleType/{id}", existingVehicleType.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVehicleType))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("BICICLETA"));
        verify(vehicleTypeService).updateVehicleType(anyLong(), any(VehicleType.class));
    }

    @Test
    @WithMockUser
    void testUpdateVehicleTypeValidationError() throws Exception {
        VehicleType existingVehicleType = DataVehicleType.createVehicleType001().orElseThrow();
        VehicleType updateVehicleType = new VehicleType(existingVehicleType.getId(), null);
        mvc.perform(put("/vehicleType/{id}", existingVehicleType.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVehicleType))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("must not be blank"));
        verify(vehicleTypeService, never()).updateVehicleType(anyLong(), any(VehicleType.class));
    }

    @Test
    @WithMockUser
    void testUpdateVehicleTypeNotFound() throws Exception {
        VehicleType existingVehicleType = DataVehicleType.createVehicleType001().orElseThrow();
        VehicleType updateVehicleType = new VehicleType(null, "BICICLETA");
        when(vehicleTypeService.updateVehicleType(existingVehicleType.getId(), updateVehicleType)).thenReturn(Optional.empty());
        mvc.perform(put("/vehicleType/{id}", existingVehicleType.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVehicleType))
                        .with(csrf()))
                .andExpect(status().isNotFound());
        verify(vehicleTypeService).updateVehicleType(anyLong(), any(VehicleType.class));
    }

    @Test
    @WithMockUser
    void deleteVehicleType() throws Exception {
        VehicleType existingVehicleType = DataVehicleType.createVehicleType001().orElseThrow();
        when(vehicleTypeService.findVehicleTypeById(existingVehicleType.getId())).thenReturn(Optional.of(existingVehicleType));
        doNothing().when(vehicleTypeService).deleteVehicleType(existingVehicleType.getId());

        mvc.perform(delete("/vehicleType/{id}", existingVehicleType.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(vehicleTypeService).findVehicleTypeById(existingVehicleType.getId());
        verify(vehicleTypeService).deleteVehicleType(existingVehicleType.getId());
    }

    @Test
    @WithMockUser
    void testDeleteVehicleTypeNotFound() throws Exception {
        VehicleType existingVehicleType = DataVehicleType.createVehicleType001().orElseThrow();
        when(vehicleTypeService.findVehicleTypeById(existingVehicleType.getId())).thenReturn(Optional.empty());
        mvc.perform(delete("/vehicleType/{id}", existingVehicleType.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(vehicleTypeService).findVehicleTypeById(existingVehicleType.getId());
        verify(vehicleTypeService, never()).deleteVehicleType(existingVehicleType.getId());
    }
}