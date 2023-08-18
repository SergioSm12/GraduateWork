package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Faculty;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Role;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;

import java.util.List;

//Creamos clase que recibe los datos del entity
public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    @JsonProperty("faculty")
    private FacultyDto faculty;
    private List<VehicleDto> vehicles;
    private boolean admin;
    private boolean guard;


    public UserDto(Long id, String name, String lastName, String email, Faculty faculty,List<VehicleDto> vehicles, boolean isAdmin, boolean isGuard) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.faculty = new FacultyDto(faculty.getId(), faculty.getNameFaculty());
        this.admin = isAdmin;
        this.guard = isGuard;
        this.vehicles=vehicles;
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

    public FacultyDto getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyDto faculty) {
        this.faculty = faculty;
    }

    public List<VehicleDto> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleDto> vehicles) {
        this.vehicles = vehicles;
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
}
