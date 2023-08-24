package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.*;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.*;

import java.util.List;
import java.util.stream.Collectors;


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

                            return  new VehicleDto(vehicle.getId(), vehicle.getPlate(), vehicleTypeDto);
                        }
                ).toList();

        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setId(receipt.getUser().getFaculty().getId());
        facultyDto.setNameFaculty(receipt.getUser().getFaculty().getNameFaculty());

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setVehicles(vehicleDtos);
        userDto.setFaculty(facultyDto);


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


        //Conversion de rate
        Rate rate = receipt.getRate();
        RateDto rateDto = new RateDto();
        rateDto.setId(rate.getId());
        rateDto.setAmount(rate.getAmount());
        rateDto.setTime(rate.getTime());


        return new ReceiptDto(this.receipt.getId(), userDto, vehicleDto, rateDto, this.receipt.getIssueDate(), this.receipt.getDueDate(), this.receipt.isPaymentStatus());
    }

}
