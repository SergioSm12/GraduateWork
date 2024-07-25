package com.sergio.spring.rest.usuariosvehiculos.app.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "visitor_receipt")
public class VisitorReceipt extends BaseReceipt {
    @NotBlank(message = "El campo placa no puede estar vacío")
    @Size(min = 5, max = 6, message = "La placa debe tener entre 5 y 6 caracteres")
    @Pattern(regexp = "^[A-Za-z]{1,3}\\d{1,3}[A-Za-z]?\\d?$", message = "El formato de la placa no es válido")
    private String plate;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public VisitorReceipt() {
        super();
    }

    public VisitorReceipt(Long id, Rate rate, LocalDateTime issueDate, LocalDateTime dueDate, boolean paymentStatus, String plate) {
        super();
        this.setId(id);
        this.setRate(rate);
        this.setIssueDate(issueDate);
        this.setDueDate(dueDate);
        this.setPaymentStatus(paymentStatus);
        this.plate = plate;
    }

}
