package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper;


import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.RateDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleTypeDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;

public class DtoMapperRate {
    private Rate rate;

    private DtoMapperRate() {
    }

    public static DtoMapperRate builder() {
        return new DtoMapperRate();
    }

    public DtoMapperRate setRate(Rate rate) {
        this.rate = rate;
        return this;
    }

    public RateDto build() {
        if (rate == null) {
            throw new RuntimeException("Debe pasar el entity rate!");
        }


        VehicleType vehicleType = this.rate.getVehicleType();
        VehicleTypeDto vehicleTypeDto = new VehicleTypeDto();
        vehicleTypeDto.setId(vehicleType.getId());
        vehicleTypeDto.setName(vehicleType.getName());

        return new RateDto(this.rate.getId(), this.rate.getTime(), this.rate.getAmount(), vehicleTypeDto);
    }
}
