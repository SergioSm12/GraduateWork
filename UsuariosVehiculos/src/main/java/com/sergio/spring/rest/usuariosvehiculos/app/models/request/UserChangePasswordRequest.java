package com.sergio.spring.rest.usuariosvehiculos.app.models.request;

import jakarta.validation.constraints.NotBlank;

public class UserChangePasswordRequest {
    @NotBlank(message = "El campo contraseña no puede estar vacío.")
    private String passwo

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
