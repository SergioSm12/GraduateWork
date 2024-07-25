package com.sergio.spring.rest.usuariosvehiculos.app.data;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;

import java.time.LocalDateTime;
import java.util.Optional;

public class DataReceipt {

    public static Optional<Receipt> createReceipt001(){
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 23, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 23, 9, 00);

        return Optional.of(new Receipt(1L,DataUser.createUser001().orElseThrow(),DataVehicle.createVehicle001().orElseThrow(),DataRate.createRate001().orElseThrow(),issueDate,dueDate,false));

    }

    public static Optional<Receipt> createReceipt002(){
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 22, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 22, 9, 00);

        return Optional.of(new Receipt(2L,DataUser.createUser002().orElseThrow(),DataVehicle.createVehicle002().orElseThrow(),DataRate.createRate002().orElseThrow(),issueDate,dueDate,true));
    }
}
