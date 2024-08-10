package com.sergio.spring.rest.usuariosvehiculos.app.models.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;

import jakarta.validation.constraints.NotNull;

public class ReceiptRequest {

    @NotNull
    private LocalDateTime issueDate;
    @NotNull
    private LocalDateTime dueDate;
    @NotNull
    private boolean paymentStatus;

    @NotNull(message = "Debe seleccionar una tarifa")
    private Rate rate;

    public ReceiptRequest() {
    }

    public ReceiptRequest(LocalDateTime issueDate, LocalDateTime dueDate, boolean paymentStatus, Rate rate) {
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.paymentStatus = paymentStatus;
        this.rate = rate;
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

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptRequest that = (ReceiptRequest) o;
        return paymentStatus == that.paymentStatus && Objects.equals(issueDate, that.issueDate) && Objects.equals(dueDate, that.dueDate) && Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueDate, dueDate, paymentStatus, rate);
    }
}
