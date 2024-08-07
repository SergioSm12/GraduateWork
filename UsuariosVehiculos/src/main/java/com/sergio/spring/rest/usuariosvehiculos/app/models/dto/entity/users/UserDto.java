package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users;


import java.util.List;
import java.util.Objects;

//Creamos clase que recibe los datos del entity
public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<VehicleDto> vehicles;
    private Boolean active;
    private boolean admin;
    private boolean guard;


    public UserDto(Long id, String name, String lastName, String email, String phoneNumber, List<VehicleDto> vehicles, Boolean active, boolean isAdmin, boolean isGuard) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.admin = isAdmin;
        this.guard = isGuard;
        this.vehicles = vehicles;
        this.active = active;
    }

    public UserDto() {
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<VehicleDto> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleDto> vehicles) {
        this.vehicles = vehicles;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isGuard() {
        return guard;
    }

    public void setGuard(boolean guard) {
        this.guard = guard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return admin == userDto.admin && guard == userDto.guard && Objects.equals(id, userDto.id) && Objects.equals(name, userDto.name) && Objects.equals(lastName, userDto.lastName) && Objects.equals(email, userDto.email) && Objects.equals(phoneNumber, userDto.phoneNumber) && Objects.equals(vehicles, userDto.vehicles) && Objects.equals(active, userDto.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, email, phoneNumber, vehicles, active, admin, guard);
    }
}
