package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataBuilding;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataCapacities;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Capacity;
import com.sergio.spring.rest.usuariosvehiculos.app.service.ICapacityService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(CapacityController.class)
class CapacityControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ICapacityService capacityService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser
    void findAllCapacity() throws Exception {
        //given
        List<Capacity> capacities = Arrays.asList(DataCapacities.createCapacity001().orElseThrow(), DataCapacities.createCapacity002().orElseThrow()
                , DataCapacities.createCapacity003().orElseThrow(), DataCapacities.createCapacity004().orElseThrow());
        when(capacityService.findAll()).thenReturn(capacities);

        //when
        mvc.perform(get("/capacity").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].maxParking").value(100))
                .andExpect(jsonPath("$[3].id").value(4))
                .andExpect(jsonPath("$[3].maxParking").value(50))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(content().json(objectMapper.writeValueAsString(capacities)));

        verify(capacityService).findAll();
    }

    @Test
    @WithMockUser
    void findCapacityById() throws Exception {
        //given
        when(capacityService.findById(1L)).thenReturn(DataCapacities.createCapacity001());

        //when
        mvc.perform(get("/capacity/1").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.building.name").value("Santo domingo"))
                .andExpect(jsonPath("$.maxParking").value(100));

        verify(capacityService).findById(anyLong());
    }

    @Test
    @WithMockUser
    void testFindCapacityByIdNotFound() throws Exception {
        //given
        Long capacityId = 1L;
        when(capacityService.findById(capacityId)).thenReturn(Optional.empty());

        //when
        mvc.perform(get("/capacity/{capacityId}", capacityId).contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNotFound());
        verify(capacityService).findById(capacityId);
    }

    @Test
    @WithMockUser
    void createCapacity() throws Exception {
        //given
        Capacity capacity = new Capacity(null, DataBuilding.createBuilding001().orElseThrow(), DataVehicleType.createVehicleType001().orElseThrow(), 77, 77, 0);
        when(capacityService.save(any(Capacity.class))).then(invocationOnMock -> {
            Capacity c = invocationOnMock.getArgument(0);
            c.setId(5L);
            return c;
        });

        //when
        mvc.perform(post("/capacity").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(capacity)).with(csrf()))
                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.building.name", is("Santo domingo")))
                .andExpect(jsonPath("$.vehicleType.name", is("MOTO")))
                .andExpect(jsonPath("$.maxParking", is(77)));

        verify(capacityService).save(any(Capacity.class));
    }

    @Test
    @WithMockUser
    void testCreateCapacityValidationErrors() throws Exception {
        //given
        Capacity invalidCapacity = new Capacity(null, null, DataVehicleType.createVehicleType001().orElseThrow(), 10, 10, 0);

        //when
        mvc.perform(post("/capacity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCapacity))
                        .with(csrf()))

                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.building").value("Debe seleccionar un edificio"));

        verify(capacityService, never()).save(any(Capacity.class));
    }

    @Test
    @WithMockUser
    void updateCapacity() throws Exception {
        //given
        Capacity existingCapacity = DataCapacities.createCapacity001().orElseThrow();
        Capacity updateCapacity = new Capacity(null, DataBuilding.createBuilding002().orElseThrow(), existingCapacity.getVehicleType(), 99, 101, 2);
        Capacity savedCapacity = new Capacity();
        savedCapacity.setId(existingCapacity.getId());
        savedCapacity.setBuilding(updateCapacity.getBuilding());
        savedCapacity.setVehicleType(updateCapacity.getVehicleType());
        savedCapacity.setParkingSpaces(updateCapacity.getParkingSpaces());
        savedCapacity.setMaxParking(updateCapacity.getMaxParking());
        savedCapacity.setOccupiedSpaces(updateCapacity.getOccupiedSpaces());

        when(capacityService.findById(existingCapacity.getId())).thenReturn(Optional.of(existingCapacity));
        when(capacityService.update(any(Capacity.class), anyLong())).thenReturn(Optional.of(savedCapacity));

        //when
        mvc.perform(put("/capacity/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCapacity))
                        .with(csrf()))

                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.building.name", is("Giordano bruno")))
                .andExpect(jsonPath("$.vehicleType.name", is("CARRO")))
                .andExpect(jsonPath("$.parkingSpaces", is(99)))
                .andExpect(jsonPath("$.maxParking", is(101)))
                .andExpect(jsonPath("$.occupiedSpaces", is(2)));

        verify(capacityService).update(any(Capacity.class), anyLong());
    }

    @Test
    @WithMockUser
    void testUpdateCapacityValidationErrors() throws Exception {
        //given
        Long capcityId = 1L;
        Capacity invalidCapacity = new Capacity(null, null, DataVehicleType.createVehicleType001().orElseThrow(), 10, 10, 0);
        when(capacityService.findById(capcityId)).thenReturn(Optional.of(new Capacity(capcityId, DataBuilding.createBuilding001().orElseThrow(), DataVehicleType.createVehicleType001().orElseThrow(), 12, 12, 0)));

        mvc.perform(put("/capacity/1", capcityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCapacity))
                        .with(csrf()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.building").value("Debe seleccionar un edificio"));

        verify(capacityService, never()).update(any(Capacity.class), anyLong());
    }

    @Test
    @WithMockUser
    void testUpdateCapacityNotFound() throws Exception {
        Long capacityId = 1L;
        Capacity updateCapacity = new Capacity(null, DataBuilding.createBuilding001().orElseThrow(), DataVehicleType.createVehicleType001().orElseThrow(), 200, 200, 0);
        when(capacityService.update(any(Capacity.class), eq(capacityId))).thenReturn(Optional.empty());

        //when
        mvc.perform(put("/capacity/1", capacityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCapacity))
                        .with(csrf()))

                //then
                .andExpect(status().isNotFound());
        verify(capacityService).update(any(Capacity.class), eq(capacityId));
    }

    @Test
    @WithMockUser
    void deleteCapacity() throws Exception {
        //given
        Capacity existCapacity = DataCapacities.createCapacity001().orElseThrow();

        when(capacityService.findById(existCapacity.getId())).thenReturn(Optional.of(existCapacity));
        doNothing().when(capacityService).deleteCapacity(existCapacity.getId());

        //when
        mvc.perform(delete("/capacity/1").with(csrf())).andExpect(status().isNoContent());

        verify(capacityService).deleteCapacity(existCapacity.getId());
    }

    @Test
    @WithMockUser
    void testDeleteCapacityNotFound() throws Exception {
        //given
        Long capacityId = 1L;
        when(capacityService.findById(capacityId)).thenReturn(Optional.empty());

        //when
        mvc.perform(delete("/capacity/1").with(csrf()))
                //then
                .andExpect(status().isNotFound());
        verify(capacityService, never()).deleteCapacity(capacityId);
    }

    @Test
    @WithMockUser
    void vehicleEntry() throws Exception {
        //given
        Long capacityId = 1L;
        doNothing().when(capacityService).vehicleEntry(capacityId);

        //when
        mvc.perform(patch("/capacity/vehicle-entry/{capacityId}", capacityId).with(csrf()))
                //then
                .andExpect(status().isOk());

        verify(capacityService).vehicleEntry(capacityId);
    }

    @Test
    @WithMockUser
    void vehicleExit() throws Exception {
        //given
        Long capacityId = 1L;
        doNothing().when(capacityService).vehicleExit(capacityId);

        //when
        mvc.perform(patch("/capacity/vehicle-exit/{capacityId}", capacityId).with(csrf()))
                //then
                .andExpect(status().isOk());

        verify(capacityService).vehicleExit(capacityId);
    }
}