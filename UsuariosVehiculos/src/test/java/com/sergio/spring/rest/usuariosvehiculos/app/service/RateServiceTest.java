package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.data.DataRate;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.RateDto;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IRateRepository;
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
class RateServiceTest {

    @MockBean
    IRateRepository rateRepository;

    @Autowired
    IRateService rateService;

    @Test
    void testRateList() {
        List<Rate> data = Arrays.asList(DataRate.createRate001().orElseThrow(), DataRate.createRate002().orElseThrow());
        when(rateRepository.findAll()).thenReturn(data);

        //when
        List<RateDto> rates = rateService.rateList();

        //then
        assertFalse(rates.isEmpty());
        assertEquals(2, rates.size());
        assertTrue(rates.stream().anyMatch(rateDto -> rateDto.getId().equals(DataRate.createRate001().orElseThrow().getId())));
        verify(rateRepository).findAll();
    }

    @Test
    void testFindRateById() {
        //given
        when(rateRepository.findById(1L)).thenReturn(DataRate.createRate001());
        when(rateRepository.findById(2L)).thenReturn(DataRate.createRate002());

        //when
        RateDto rate1 = rateService.findRateById(1L).orElseThrow();
        RateDto rate2 = rateService.findRateById(2L).orElseThrow();

        assertEquals(1L, rate1.getId());
        assertEquals("DIA MOTO", rate1.getTime());
        assertEquals(3000, rate1.getAmount());
        assertEquals("MOTO", rate1.getVehicleType().getName());

        assertEquals(2L, rate2.getId());
        assertEquals("DIA CARRO", rate2.getTime());
        assertEquals(5000, rate2.getAmount());
        assertEquals("CARRO", rate2.getVehicleType().getName());
    }

    @Test
    void testCreateRate() {
        //given
        Rate newRate = new Rate(null, DataVehicleType.createVehicleType001().orElseThrow(), "MES MOTO", 70000);
        when(rateRepository.save(any())).then(invocationOnMock -> {
            Rate r = invocationOnMock.getArgument(0);
            r.setId(3L);
            return r;
        });

        //when
        RateDto rate = rateService.createRate(newRate);

        //then
        assertEquals(3L, rate.getId());
        assertEquals("MOTO", rate.getVehicleType().getName());
        assertEquals("MES MOTO", rate.getTime());
        assertEquals(70000, rate.getAmount());
        verify(rateRepository).save(any());
    }

    @Test
    void testUpdate() {
        //given
        Rate existsRate = DataRate.createRate001().orElseThrow();
        Rate updateRate = new Rate(existsRate.getId(), DataVehicleType.createVehicleType002().orElseThrow(), "MES CARRO", 85000);
        when(rateRepository.findById(existsRate.getId())).thenReturn(Optional.of(existsRate));
        when(rateRepository.save(any())).thenReturn(updateRate);

        //when
        Optional<RateDto> result = rateService.update(updateRate, existsRate.getId());

        //then
        assertTrue(result.isPresent());
        assertEquals("CARRO", result.get().getVehicleType().getName());
        assertEquals("MES CARRO", result.get().getTime());
        assertEquals(85000, result.get().getAmount());
        verify(rateRepository).save(any());
    }

    @Test
    void deleteRate() {
        Long rateId = DataRate.createRate001().orElseThrow().getId();
        doNothing().when(rateRepository).deleteById(rateId);

        rateService.deleteRate(rateId);

        verify(rateRepository).deleteById(rateId);
    }
}