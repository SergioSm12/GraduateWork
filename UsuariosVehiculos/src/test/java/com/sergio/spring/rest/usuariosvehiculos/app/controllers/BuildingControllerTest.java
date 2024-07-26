package com.sergio.spring.rest.usuariosvehiculos.app.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataBuilding;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Building;

import com.sergio.spring.rest.usuariosvehiculos.app.service.IBuildingService;
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

@WebMvcTest(BuildingController.class)
class BuildingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IBuildingService buildingService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser
//simula un usuario autenticado.
    void getBuilding() throws Exception {
        List<Building> buildings = Arrays.asList(DataBuilding.createBuilding001().orElseThrow(), DataBuilding.createBuilding002().orElseThrow());
        when(buildingService.buildingList()).thenReturn(buildings);

        //when
        mvc.perform(get("/building").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Santo domingo"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Giordano bruno"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(buildings)));

        verify(buildingService).buildingList();
    }

    @Test
    @WithMockUser
    void findBuildingBYId() throws Exception {
        //given
        when(buildingService.findBuildingById(1L)).thenReturn(DataBuilding.createBuilding001());

        //when
        mvc.perform(get("/building/1").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Santo domingo"))
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    @WithMockUser
    void testFindBuildingByIdNotFound() throws Exception {
        //given
        Long buildingId = 1L;
        when(buildingService.findBuildingById(buildingId)).thenReturn(Optional.empty());

        //when
        mvc.perform(get("/building/{id}", buildingId).contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNotFound());

        verify(buildingService).findBuildingById(buildingId);
    }

    @Test
    @WithMockUser
    void createBuilding() throws Exception {
        //given
        Building building = new Building(null, "Casa tomasina");
        when(buildingService.createBuilding(any(Building.class))).then(invocationOnMock -> {
            Building b = invocationOnMock.getArgument(0);
            b.setId(3L);
            return b;
        });

        //when
        mvc.perform(post("/building").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(building)).with(csrf()))

                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Casa tomasina")));

        verify(buildingService).createBuilding(any(Building.class));
    }

    @Test
    @WithMockUser
    void createBuildingValidationErrors() throws Exception {
        //given
        Building invalidBuilding = new Building(null, "");

        //when
        mvc.perform(post("/building")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBuilding))
                        .with(csrf()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("El nombre no puede estar vacío"));

        verify(buildingService, never()).createBuilding(any(Building.class));
    }

    @Test
    @WithMockUser
    void updateBuilding() throws Exception {
        //given
        Building existingBuilding = DataBuilding.createBuilding001().orElseThrow();
        Building updateBuilding = new Building(null, "Casa actualizada");
        Building savedBuilding = new Building();
        savedBuilding.setId(existingBuilding.getId());
        savedBuilding.setName(updateBuilding.getName());

        when(buildingService.findBuildingById(existingBuilding.getId())).thenReturn(Optional.of(existingBuilding));
        when(buildingService.update(any(Building.class), anyLong())).thenReturn(Optional.of(savedBuilding));

        //when
        mvc.perform(put("/building/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBuilding))
                        .with(csrf()))
                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Casa actualizada")));

        verify(buildingService).update(any(Building.class), anyLong());
    }

    @Test
    @WithMockUser
    void updateBuildingValidationErrors() throws Exception {
        // given
        Long buildingId = 1L;
        Building invalidBuilding = new Building(null, "");

        when(buildingService.findBuildingById(buildingId)).thenReturn(Optional.of(new Building(buildingId, "Old Name")));

       //when
        mvc.perform(put("/building/{id}", buildingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBuilding))
                        .with(csrf()))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("El nombre no puede estar vacío"));

        verify(buildingService, never()).update(any(Building.class), anyLong());
    }

    @Test
    @WithMockUser
    void updateBuildingNotFound() throws Exception {
        // given
        Long buildingId = 1L;
        Building updateBuilding = new Building(null, "Edificio actualizado");
        when(buildingService.update(any(Building.class), eq(buildingId))).thenReturn(Optional.empty());

        // when
        mvc.perform(put("/building/{id}", buildingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBuilding))
                        .with(csrf()))
                // then
                .andExpect(status().isNotFound());

        verify(buildingService).update(any(Building.class), eq(buildingId));
    }

    @Test
    @WithMockUser
    void deleteBuilding() throws Exception {
        //given
        Building existBuilding = DataBuilding.createBuilding001().orElseThrow();

        when(buildingService.findBuildingById(existBuilding.getId())).thenReturn(Optional.of(existBuilding));
        doNothing().when(buildingService).deleteBuilding(existBuilding.getId());

        //when
        mvc.perform(delete("/building/1").with(csrf())).andExpect(status().isNoContent());

        verify(buildingService).deleteBuilding(existBuilding.getId());
    }

    @Test
    @WithMockUser
    void deleteBuildingNotFound() throws Exception {
        // given
        Long buildingId = 1L;
        when(buildingService.findBuildingById(buildingId)).thenReturn(Optional.empty());

        // when
        mvc.perform(delete("/building/{id}", buildingId)
                        .with(csrf()))
                // then
                .andExpect(status().isNotFound());

        verify(buildingService, never()).deleteBuilding(buildingId);
    }

}