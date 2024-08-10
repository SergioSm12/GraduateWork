package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataRate;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.RateDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperRate;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IRateService;
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
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(RateController.class)
class RateControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    IRateService rateService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser
    void rateList() throws Exception {
        //given
        List<Rate> rates = Arrays.asList(DataRate.createRate001().orElseThrow(), DataRate.createRate002().orElseThrow()
                , DataRate.createRate003().orElseThrow(), DataRate.createRate004().orElseThrow(), DataRate.createRate005().orElseThrow());
        List<RateDto> rateDtos = rates.stream().map(rate -> DtoMapperRate.builder().setRate(rate).build()).collect(Collectors.toList());
        when(rateService.rateList()).thenReturn(rateDtos);

        //when
        mvc.perform(get("/rate").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].time").value("DIA MOTO"))
                .andExpect(jsonPath("$[0].amount").value(3000))
                .andExpect(jsonPath("$[4].id").value(5))
                .andExpect(jsonPath("$[4].time").value("VISITANTE CARRO"))
                .andExpect(jsonPath("$[4].amount").value(5000))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(content().json(objectMapper.writeValueAsString(rateDtos)));

        verify(rateService).rateList();

    }

    @Test
    @WithMockUser
    void findRateById() throws Exception {
        //given
        Rate rate = DataRate.createRate001().orElseThrow();
        RateDto rateDto = DtoMapperRate.builder().setRate(rate).build();
        when(rateService.findRateById(1L)).thenReturn(Optional.of(rateDto));

        //when
        mvc.perform(get("/rate/{rateId}", rate.getId()).contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.time").value("DIA MOTO"))
                .andExpect(jsonPath("$.amount").value(3000));

        verify(rateService).findRateById(anyLong());
    }

    @Test
    @WithMockUser
    void findRateByIdNotFound() throws Exception {
        //given
        Rate rate = DataRate.createRate001().orElseThrow();
        when(rateService.findRateById(1L)).thenReturn(Optional.empty());

        //when
        mvc.perform(get("/rate/{rateId}", rate.getId()).contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNotFound());
        verify(rateService).findRateById(anyLong());
    }

    @Test
    @WithMockUser
    void createRate() throws Exception {
        //given
        Rate rate = new Rate(null, DataVehicleType.createVehicleType001().orElseThrow(), "MES MOTO", 30000);
        when(rateService.createRate(any(Rate.class))).then(invocationOnMock -> {
            Rate r = invocationOnMock.getArgument(0);
            r.setId(6L);
            RateDto rateDto = DtoMapperRate.builder().setRate(r).build();
            return rateDto;
        });

        //when
        mvc.perform(post("/rate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rate)).with(csrf()))
                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$.vehicleType.name", is("MOTO")))
                .andExpect(jsonPath("$.time", is("MES MOTO")))
                .andExpect(jsonPath("$.amount", is(30000.0)));
        verify(rateService).createRate(any(Rate.class));

    }

    @Test
    @WithMockUser
    void testCreateRateValidationErrors() throws Exception {
        //given
        Rate invalidRate = new Rate(null, null, "QUINCENA MOTO", 10000);
        //When
        mvc.perform(post("/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRate))
                        .with(csrf()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vehicleType").value("Debe seleccionar un tipo de vehiculo."));

        verify(rateService, never()).createRate(any(Rate.class));

    }

    @Test
    @WithMockUser
    void updateRate() throws Exception {
        //given
        Rate existingRate = DataRate.createRate001().orElseThrow();
        RateDto rateDto = DtoMapperRate.builder().setRate(existingRate).build();
        Rate updateRate = new Rate(null, existingRate.getVehicleType(), "TARIFA ACTUALIZADA", 80000);
        Rate savedRate = new Rate();
        savedRate.setId(existingRate.getId());
        savedRate.setVehicleType(updateRate.getVehicleType());
        savedRate.setTime(updateRate.getTime());
        savedRate.setAmount(updateRate.getAmount());
        RateDto rateDtoUpdate = DtoMapperRate.builder().setRate(savedRate).build();

        when(rateService.findRateById(existingRate.getId())).thenReturn(Optional.of(rateDto));
        when(rateService.update(any(Rate.class), anyLong())).thenReturn(Optional.of(rateDtoUpdate));

        //when
        mvc.perform(put("/rate/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRate))
                        .with(csrf()))
                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.time", is("TARIFA ACTUALIZADA")))
                .andExpect(jsonPath("$.amount", is(80000.0)));
        verify(rateService).update(any(Rate.class), anyLong());
    }

    @Test
    @WithMockUser
    void testUpdateRateValidationErrors() throws Exception {
        //GIVEN
        Rate rate = DataRate.createRate001().orElseThrow();
        RateDto rateDto = DtoMapperRate.builder().setRate(rate).build();
        Rate invalidRate = new Rate(null, null, "TARIFA INVALIA", 40000);
        when(rateService.findRateById(rate.getId())).thenReturn(Optional.of(rateDto));

        mvc.perform(put("/rate/{rateId}", rate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRate))
                        .with(csrf()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vehicleType").value("Debe seleccionar un tipo de vehiculo."));

        verify(rateService, never()).update(any(Rate.class), anyLong());
    }

    @Test
    @WithMockUser
    void testUpdateRateNotFound() throws Exception {
        //given
        Rate existingRate = DataRate.createRate001().orElseThrow();
        RateDto rateDto = DtoMapperRate.builder().setRate(existingRate).build();
        Rate updateRate = new Rate(null, existingRate.getVehicleType(), "TARIFA ACTUALIZADA", 80000);
        when(rateService.update(any(Rate.class), eq(existingRate.getId()))).thenReturn(Optional.empty());

        //when
        mvc.perform(put("/rate/{rateId}", existingRate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRate)).with(csrf()))
                //then
                .andExpect(status().isNotFound());
        verify(rateService).update(any(Rate.class), eq(existingRate.getId()));
    }

    @Test
    @WithMockUser
    void deleteRate() throws Exception {
        Rate existingRate = DataRate.createRate001().orElseThrow();
        RateDto rateDto = DtoMapperRate.builder().setRate(existingRate).build();
        when(rateService.findRateById(existingRate.getId())).thenReturn(Optional.of(rateDto));
        doNothing().when(rateService).deleteRate(existingRate.getId());

        //when
        mvc.perform(delete("/rate/1").with(csrf())).andExpect(status().isNoContent());

        verify(rateService).deleteRate(existingRate.getId());
    }

    @Test
    @WithMockUser
    void testDeleteRateNotFound() throws Exception {
        Long rateId = 1L;
        when(rateService.findRateById(rateId)).thenReturn(Optional.empty());

        //when
        mvc.perform(delete("/rate/1").with(csrf()))
                //then
                .andExpect(status().isNotFound());

        verify(rateService, never()).deleteRate(rateId);
    }
}