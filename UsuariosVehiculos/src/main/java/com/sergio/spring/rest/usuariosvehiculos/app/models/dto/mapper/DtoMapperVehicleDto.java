package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleTypeDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;


public class DtoMapperVehicleDto {
    private Vehicle vehicle;

    private DtoMapperVehicleDto() {
    }

    public static DtoMapperVehicleDto builder() {
        return new DtoMapperVehicleDto();
    }

    public DtoMapperVehicleDto setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }


    public VehicleDto build() {
        if (vehicle == null) {
            throw new RuntimeException("Debe pasar el entity vehicle");
        }

        User user = vehicle.getUser();

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());

        //conversion de vehicle
        VehicleType vehicleType = vehicle.getVehicleType();

        VehicleTypeDto vehicleTypeDto = new VehicleTypeDto();
        vehicleTypeDto.setId(vehicleType.getId());
        vehicleTypeDto.setName(vehicleType.getName());


        return new VehicleDto(this.vehicle.getId(), this.vehicle.getPlate(), userDto, vehicleTypeDto, vehicle.isActive());
    }


}
