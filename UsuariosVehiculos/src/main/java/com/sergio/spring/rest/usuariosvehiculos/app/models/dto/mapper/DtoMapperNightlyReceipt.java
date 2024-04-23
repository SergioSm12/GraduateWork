package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.*;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.*;

import java.util.List;

public class DtoMapperNightlyReceipt {
    private NightlyReceipt nightlyReceipt;

    private DtoMapperNightlyReceipt() {
    }

    public static DtoMapperNightlyReceipt builder() {
        return new DtoMapperNightlyReceipt();
    }

    public DtoMapperNightlyReceipt setNightlyReceipt(NightlyReceipt nightlyReceipt) {
        this.nightlyReceipt = nightlyReceipt;
        return this;
    }

    public NightlyReceiptDto build() {
        if (nightlyReceipt == null) {
            throw new RuntimeException("Debe pasar el entity Nigthly Receipt");
        }


        //Conversion de user

        User user = nightlyReceipt.getUser();

        List<VehicleDto> vehicleDtos = this.nightlyReceipt.getUser().getVehicles().stream()
                .map(vehicle -> {
                            VehicleTypeDto vehicleTypeDto = new VehicleTypeDto(
                                    vehicle.getVehicleType().getId(),
                                    vehicle.getVehicleType().getName()
                            );


                            return new VehicleDto(vehicle.getId(), vehicle.getPlate(), null, vehicleTypeDto, vehicle.isActive());
                        }
                ).toList();


        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setVehicles(vehicleDtos);
        userDto.setPhoneNumber(user.getPhoneNumber());


        //Conversion de vehicle
        Vehicle vehicle = nightlyReceipt.getVehicle();
        VehicleType vehicleType = vehicle.getVehicleType();
        VehicleTypeDto vehicleTypeDto = new VehicleTypeDto();
        vehicleTypeDto.setId(vehicleType.getId());
        vehicleTypeDto.setName(vehicleType.getName());
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setId(vehicle.getId());
        vehicleDto.setPlate(vehicle.getPlate());
        vehicleDto.setVehicleType(vehicleTypeDto);
        vehicleDto.setActive(vehicle.isActive());


        //Conversion de rate
        Rate rate = nightlyReceipt.getRate();
        RateDto rateDto = new RateDto();
        rateDto.setId(rate.getId());
        rateDto.setAmount(rate.getAmount());
        rateDto.setTime(rate.getTime());


        return new NightlyReceiptDto(this.nightlyReceipt.getId(), userDto, vehicleDto, rateDto, this.nightlyReceipt.getInitialTime(), this.nightlyReceipt.getDepartureTime(), this.nightlyReceipt.isPaymentStatus(), this.nightlyReceipt.getAmount());
    }
}
