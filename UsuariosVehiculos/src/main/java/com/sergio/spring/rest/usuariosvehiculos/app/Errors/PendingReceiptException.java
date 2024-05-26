package com.sergio.spring.rest.usuariosvehiculos.app.Errors;

public class PendingReceiptException extends  RuntimeException{
    public PendingReceiptException(String message){
        super(message);
    }
}
