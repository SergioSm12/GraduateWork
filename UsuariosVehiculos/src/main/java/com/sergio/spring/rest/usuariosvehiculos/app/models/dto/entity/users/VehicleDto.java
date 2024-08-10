package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class VehicleDto {
    private Long id;
    private String plate;

    @JsonProperty("user")
    private UserDto userDto;
    private VehicleTypeDto vehicleType;
    private Boolean active;

    public VehicleDto() {
    }

    public VehicleDto(Long id, String plate, UserDto userDto, VehicleTypeDto vehicleType, boolean active) {
        this.id = id;
        this.plate = plate;
        this.vehicleType = vehicleType;
        this.active = active;
        this.userDto = userDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }


    public VehicleTypeDto getVehicleType() {
        return vehicleType;
    }


    public void setVehicleType(VehicleTypeDto vehicleType) {
        this.vehicleType = vehicleType;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleDto that = (VehicleDto) o;
        return Objects.equals(id, that.id) && Objects.equals(plate, that.plate) && Objects.equals(userDto, that.userDto) && Objects.equals(vehicleType, that.vehicleType) && Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plate, userDto, vehicleType, active);
    }
}
