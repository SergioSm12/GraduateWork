package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper;

import java.util.List;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.RateDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.ReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleTypeDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;


public class DtoMapperReceipt {

    private Receipt receipt;

    private DtoMapperReceipt() {

    }

    public static DtoMapperReceipt builder() {
        return new DtoMapperReceipt();
    }

    public DtoMapperReceipt setReceipt(Receipt receipt) {
        this.receipt = receipt;
        return this;
    }

    public ReceiptDto build() {
        if (receipt == null) {
            throw new RuntimeException("Debe pasar el entity receipt");
        }


        //Conversion de user

        User user = receipt.getUser();

        List<VehicleDto> vehicleDtos = this.receipt.getUser().getVehicles().stream()
                .map(vehicle -> {
                            VehicleTypeDto vehicleTypeDto = new VehicleTypeDto(
                                    vehicle.getVehicleType().getId(),
                                    vehicle.getVehicleType().getName()
                            );



                            return  new VehicleDto(vehicle.getId(), vehicle.getPlate(),null, vehicleTypeDto, vehicle.isActive());
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
        Vehicle vehicle = receipt.getVehicle();
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
        Rate rate = receipt.getRate();
        RateDto rateDto = new RateDto();
        rateDto.setId(rate.getId());
        rateDto.setAmount(rate.getAmount());
        rateDto.setTime(rate.getTime());


        return new ReceiptDto(this.receipt.getId(), userDto, vehicleDto, rateDto, this.receipt.getIssueDate(), this.receipt.getDueDate(), this.receipt.isPaymentStatus());
    }

}
