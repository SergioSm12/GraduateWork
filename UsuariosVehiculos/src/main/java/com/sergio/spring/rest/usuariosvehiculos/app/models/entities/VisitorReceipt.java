package com.sergio.spring.rest.usuariosvehiculos.app.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "visitor_receipt")
public class VisitorReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo placa no puede estar vacío")
    @Size(min = 5, max = 6, message = "La placa debe tener entre 5 y 6 caracteres")
    @Pattern(regexp = "^[A-Za-z]{1,3}\\d{1,3}[A-Za-z]?\\d?$", message = "El formato de la placa no es válido")
    private String plate;

    @NotNull(message = "Debe seleccionar una tarifa")
    @ManyToOne
    @JoinColumn(name = "rate_id")
    private Rate rate;

    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private boolean paymentStatus;

    public VisitorReceipt() {

    }

    public VisitorReceipt(Long id, Rate rate, LocalDateTime issueDate, LocalDateTime dueDate, boolean paymentStatus, String plate) {
        this.setId(id);
        this.setRate(rate);
        this.setIssueDate(issueDate);
        this.setDueDate(dueDate);
        this.setPaymentStatus(paymentStatus);
        this.plate = plate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitorReceipt that = (VisitorReceipt) o;
        return paymentStatus == that.paymentStatus && Objects.equals(id, that.id) && Objects.equals(plate, that.plate) && Objects.equals(rate, that.rate) && Objects.equals(issueDate, that.issueDate) && Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plate, rate, issueDate, dueDate, paymentStatus);
    }
}
