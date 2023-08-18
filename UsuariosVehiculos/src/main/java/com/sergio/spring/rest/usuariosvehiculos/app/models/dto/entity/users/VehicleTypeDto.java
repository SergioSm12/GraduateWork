package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users;

public class VehicleTypeDto {
    private  Long id;
    private String name;

    public VehicleTypeDto() {
    }

    public VehicleTypeDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
