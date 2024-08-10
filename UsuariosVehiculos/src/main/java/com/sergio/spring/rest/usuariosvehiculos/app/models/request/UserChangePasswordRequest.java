package com.sergio.spring.rest.usuariosvehiculos.app.models.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class UserChangePasswordRequest {
    @NotBlank(message = "El campo contraseña no puede estar vacío.")
    private String password;

    public UserChangePasswordRequest() {
    }

    public UserChangePasswordRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserChangePasswordRequest that = (UserChangePasswordRequest) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(password);
    }
}
