package com.sergio.spring.rest.usuariosvehiculos.app.Errors;

public class CapacityNotFoundException extends  RuntimeException{
    public  CapacityNotFoundException(String message){
        super(message);
    }
}
