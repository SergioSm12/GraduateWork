package com.sergio.spring.rest.usuariosvehiculos.app.auth.dto;

public class AuthResponse {
    private String message;
    private String user;
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(String message, String user, String token) {
        this.message = message;
        this.user = user;
        this.token = token;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
