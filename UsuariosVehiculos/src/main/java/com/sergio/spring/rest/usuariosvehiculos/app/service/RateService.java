package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.RateDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperRate;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RateService implements IRateService {
    @Autowired
    private IRateRepository rateRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RateDto> rateList() {
        List<Rate> rates = (List<Rate>) rateRepository.findAll();
        return rates.stream().map(r -> DtoMapperRate
                        .builder()
                        .setRate(r).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RateDto> findRateById(Long rateId) {
        return rateRepository.findById(rateId).map(
                r -> DtoMapperRate
                        .builder()
                        .setRate(r)
                        .build());
    }

    @Transactional
    public RateDto createRate(Rate rate) {
        String timeUpper = rate.getTime().toUpperCase();
        rate.setTime(timeUpper);
        return DtoMapperRate.builder().setRate(rateRepository.save(rate)).build();
    }

    @Override
    @Transactional
    public Optional<RateDto> update(Rate rate, Long id) {
        Optional<Rate> ro = rateRepository.findById(id);
        Rate rateOptional = null;
        if (ro.isPresent()) {
            Rate rateDB = ro.orElseThrow();
            rateDB.setTime(rate.getTime().toUpperCase());
            rateDB.setAmount(rate.getAmount());
            rateDB.setVehicleType(rate.getVehicleType());
            rateOptional = rateRepository.save(rateDB);
        }
        return Optional.ofNullable(DtoMapperRate.builder().setRate(rateOptional).build());
    }

    @Override
    @Transactional
    public void deleteRate(Long id) {
        rateRepository.deleteById(id);
    }

}
