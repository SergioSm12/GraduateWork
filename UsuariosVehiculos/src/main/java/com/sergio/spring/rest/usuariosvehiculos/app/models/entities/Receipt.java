package com.sergio.spring.rest.usuariosvehiculos.app.models.entities;



import jakarta.persistence.Entity;

import jakarta.persistence.Table;

import java.time.LocalDateTime;


@Entity
@Table(name = "receipts")
public class Receipt extends BaseReceipt {

    public Receipt() {
        super();
    }

    public Receipt(Long id, User user, Vehicle vehicle, Rate rate, LocalDateTime issueDate, LocalDateTime dueDate, boolean paymentStatus) {
        super();
        this.setId(id);
        this.setUser(user);
        this.setVehicle(vehicle);
        this.setRate(rate);
        this.setIssueDate(issueDate);
        this.setDueDate(dueDate);
        this.setPaymentStatus(paymentStatus);
    }


}
