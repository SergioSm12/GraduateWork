package com.sergio.spring.rest.usuariosvehiculos.app.models.request;

import com.sergio.spring.rest.usuariosvehiculos.app.models.interfaces.IUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

//Clase generada para el update solo pasar campos necesarios
public class UserRequest implements IUser {
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phoneNumber;
    private boolean admin;
    private boolean guard;

    public UserRequest() {
    }

    public UserRequest(String name, String lastName, String email, String phoneNumber, boolean admin, boolean guard) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.admin = admin;
        this.guard = guard;
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

    @Override
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
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
        UserRequest that = (UserRequest) o;
        return admin == that.admin && guard == that.guard && Objects.equals(name, that.name) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, email, phoneNumber, admin, guard);
    }
}
