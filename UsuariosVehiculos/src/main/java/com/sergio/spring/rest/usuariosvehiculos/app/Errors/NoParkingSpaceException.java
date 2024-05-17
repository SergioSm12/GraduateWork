package com.sergio.spring.rest.usuariosvehiculos.app.Errors;

public class NoParkingSpaceException extends  RuntimeException{
    public  NoParkingSpaceException(String message){
        super(message);
    }
}
