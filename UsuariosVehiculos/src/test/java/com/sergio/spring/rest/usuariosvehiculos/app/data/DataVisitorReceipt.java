package com.sergio.spring.rest.usuariosvehiculos.app.data;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;

import java.time.LocalDateTime;
import java.util.Optional;

public class DataVisitorReceipt {
    public static Optional<VisitorReceipt> createVisitorReceipt001(){
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 24, 11, 00);
        return Optional.of(new VisitorReceipt(1L,DataRate.createRate004().orElseThrow(),issueDate,dueDate,false,"SZP85E"));
    }

    public static Optional<VisitorReceipt> createVisitorReceipt002(){
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 24, 9, 00);
        return Optional.of(new VisitorReceipt(2L,DataRate.createRate005().orElseThrow(),issueDate,dueDate,true,"QSA741"));
    }
}
