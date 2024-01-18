package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.util.List;
import java.util.Optional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.RateDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;

public interface IRateService {
    List<RateDto> rateList();

    Optional<RateDto> findRateById(Long rateId);

    RateDto createRate(Rate rate);

    Optional<RateDto> update(Rate rate, Long id);

    void deleteRate(Long id);
}
